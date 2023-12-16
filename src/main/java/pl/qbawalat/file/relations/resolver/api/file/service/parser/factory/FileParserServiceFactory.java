package pl.qbawalat.file.relations.resolver.api.file.service.parser.factory;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;
import pl.qbawalat.file.relations.resolver.api.file.enums.ConsumeableFileType;
import pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.FileParser;
import pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv.CsvFileParserService;

@Service
@RequiredArgsConstructor
public class FileParserServiceFactory {
    private final Map<String, FileParser<MultipartFile, ParsedFile>> filerParserBeansByName;

    private static String toParserBeanId(String fileType) {
        ConsumeableFileType supportedFileType = ConsumeableFileType.fromMimeType(fileType);
        return switch (supportedFileType) {
            case CSV -> CsvFileParserService.BEAN_ID;
        };
    }

    public FileParser<MultipartFile, ParsedFile> getFileParser(String fileType) {
        String parserBeanId = toParserBeanId(fileType);
        return filerParserBeansByName.get(parserBeanId);
    }
}
