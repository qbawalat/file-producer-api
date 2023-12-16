package pl.qbawalat.file.relations.resolver.api.file.service.identifier;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;
import pl.qbawalat.file.relations.resolver.api.file.domain.RelatedFilesContainer;
import pl.qbawalat.file.relations.resolver.api.file.util.ParsedFileAnalyzer;

@Service
@Validated
public class FileIdentifierService {
    public RelatedFilesContainer identifyFiles(@NotEmpty List<ParsedFile> parsedFiles) {
        return splitFilesByType(parsedFiles);
    }

    private RelatedFilesContainer splitFilesByType(List<ParsedFile> parsedFiles) {
        Set<String> allColumns =
                parsedFiles.stream().map(ParsedFileAnalyzer::getColumnsNames).collect(Collectors.toSet()).stream()
                        .flatMap(Set::stream)
                        .collect(Collectors.toSet());
        Map<Boolean, List<ParsedFile>> partitionedFiles = parsedFiles.stream()
                .collect(Collectors.partitioningBy(file -> isAnyColumnRelatedToFile(file, allColumns)));

        return new RelatedFilesContainer(partitionedFiles.get(false), partitionedFiles.get(true));
    }

    private boolean isAnyColumnRelatedToFile(ParsedFile file, Set<String> allColumns) {
        return allColumns.contains(ParsedFileAnalyzer.getForeignKeyName(file));
    }
}
