package pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv;

import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFileContent;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedRecord;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileParseException;
import pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.FileParser;
import pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv.service.ParserCreatorService;
import pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv.service.RecordParserService;
import pl.qbawalat.file.relations.resolver.api.file.util.CollectionsToolbox;

@Service(CsvFileParserService.BEAN_ID)
@RequiredArgsConstructor
public class CsvFileParserService implements FileParser<MultipartFile, ParsedFile> {
    public static final String BEAN_ID = "csvFileParserService";

    private final ParserCreatorService parserCreator;
    private final RecordParserService recordParser;

    @Override
    public List<ParsedFile> parseFiles(List<MultipartFile> multipartFiles) throws FileParseException {
        return multipartFiles.stream().map(this::parse).toList();
    }

    @Override
    public ParsedFile parse(MultipartFile multipartFile) {
        try (CSVParser parser = parserCreator.createParser(multipartFile.getInputStream())) {
            List<String> headersNames = resolveHeadersNames(parser);
            List<ParsedRecord> parsedRecords = parser.getRecords().stream()
                    .map(currentRecord -> recordParser.parseRecord(headersNames, currentRecord))
                    .toList();

            return new ParsedFile(multipartFile.getOriginalFilename(), new ParsedFileContent(parsedRecords));
        } catch (IOException e) {
            throw new FileParseException("Error while parsing file: " + multipartFile.getOriginalFilename(), e);
        }
    }

    private List<String> resolveHeadersNames(CSVParser parser) {
        List<String> headers = parser.getHeaderNames();
        if (CollectionsToolbox.hasUniqueValues(headers)) {
            return headers;
        } else {
            throw new FileParseException("Duplicated headers in file.");
        }
    }
}
