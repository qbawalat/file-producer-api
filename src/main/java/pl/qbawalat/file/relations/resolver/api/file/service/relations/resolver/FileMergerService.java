package pl.qbawalat.file.relations.resolver.api.file.service.relations.resolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedRecord;
import pl.qbawalat.file.relations.resolver.api.file.domain.ResolvedRelationFile;
import pl.qbawalat.file.relations.resolver.api.file.enums.ColumnHeader;
import pl.qbawalat.file.relations.resolver.api.file.enums.ParsedFileType;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileRelationException;
import pl.qbawalat.file.relations.resolver.api.file.service.relations.resolver.column.RelatedColumnResolverService;
import pl.qbawalat.file.relations.resolver.api.file.util.ParsedFileAnalyzer;

@Service
@RequiredArgsConstructor
public class FileMergerService {

    private final RelatedColumnResolverService relatedColumnResolver;

    public ResolvedRelationFile mergeFiles(ParsedFile mainFile, List<ParsedFile> supplierFiles) {
        Map<String, Map<String, ParsedRecord>> supplierFilesByCodeByFileName = toSupplierFiles(supplierFiles);

        return new ResolvedRelationFile(
                mainFile.fileName(),
                ParsedFileType.MAIN,
                mainFile.content().records().stream()
                        .map(mainFileRecord -> {
                            ParsedRecord clonedRecord = (ParsedRecord) mainFileRecord.clone();

                            supplierFilesByCodeByFileName.forEach((supplierFileName, supplierFileByCode) -> {
                                String relationColumnName = ParsedFileAnalyzer.getForeignKeyName(supplierFileName);
                                String relationColumnValue = getRelationColumnValue(mainFileRecord, relationColumnName);
                                relatedColumnResolver
                                        .resolveRelatedColumnValue(supplierFileByCode, relationColumnValue)
                                        .ifPresent(parsedRecord -> clonedRecord.put(relationColumnName, parsedRecord));
                            });
                            return clonedRecord;
                        })
                        .toList());
    }

    private Map<String, Map<String, ParsedRecord>> toSupplierFiles(List<ParsedFile> parsedFiles) {
        return parsedFiles.stream()
                .collect(Collectors.toMap(ParsedFile::fileName, parsedFile -> parsedFile.content().records().stream()
                        .collect(Collectors.toMap(
                                value -> (String) value.get(ColumnHeader.CODE.getValue()), value -> value))));
    }

    private String getRelationColumnValue(ParsedRecord mainFileRecord, String relationColumnName) {
        Object relationColumnValue = mainFileRecord.get(relationColumnName);
        if (relationColumnValue instanceof String typeGuardedColumnValue) {
            return typeGuardedColumnValue;
        } else {
            throw new FileRelationException(
                    "The value in the column that links two files must be a String type. Column name: "
                            + relationColumnName);
        }
    }
}
