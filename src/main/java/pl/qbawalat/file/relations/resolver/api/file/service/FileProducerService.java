package pl.qbawalat.file.relations.resolver.api.file.service;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;
import pl.qbawalat.file.relations.resolver.api.file.domain.RelatedFilesContainer;
import pl.qbawalat.file.relations.resolver.api.file.domain.ResolvedRelationFile;
import pl.qbawalat.file.relations.resolver.api.file.dto.FileDto;
import pl.qbawalat.file.relations.resolver.api.file.service.identifier.FileIdentifierService;
import pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.FileParserServiceFactory;
import pl.qbawalat.file.relations.resolver.api.file.service.relations.resolver.FileMergerService;
import pl.qbawalat.file.relations.resolver.api.file.service.sorter.FileRecordsSorterService;

/**
 * Converts multiple csv files into single json file.
 */
@Service
@RequiredArgsConstructor
public class FileProducerService {
    private final FileParserServiceFactory fileParserFactory;
    private final FileMergerService fileRelationsResolver;
    private final FileIdentifierService fileIdentifier;
    private final FileRecordsSorterService fileRecordsSorter;

    public FileDto processFiles(List<MultipartFile> multipartFiles) {
        List<ParsedFile> parsedFiles = parseFiles(multipartFiles);
        RelatedFilesContainer relatedFilesContainer = fileIdentifier.identifyFiles(parsedFiles);
        ResolvedRelationFile resolvedRelationFile = fileRelationsResolver.mergeFiles(
                relatedFilesContainer.mainFiles().get(0), relatedFilesContainer.supplierFiles());
        return new FileDto(fileRecordsSorter.sortRecords(resolvedRelationFile.entries()));
    }

    private List<ParsedFile> parseFiles(List<MultipartFile> multipartFiles) {
        String fileType = Objects.requireNonNull(multipartFiles.get(0).getContentType());
        return fileParserFactory.getFileParser(fileType).parseFiles(multipartFiles);
    }
}
