package pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv;

import static org.junit.jupiter.api.Assertions.*;
import static pl.qbawalat.file.relations.resolver.api.util.MultipartFileMocker.createMockMultipartFileFromCsv;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileParseException;
import pl.qbawalat.file.relations.resolver.api.util.ParsedFileMocker;

@SpringBootTest
class CsvFileParserServiceTest {
    private static final String BASE_PATH = "dataset/parser.service/";

    @Autowired
    private CsvFileParserService csvFileParser;

    @Test
    void fileWithDuplicatedHeadersThrowsFileParseException() throws IOException {
        MockMultipartFile mockMultipartFileFromCsv =
                createMockMultipartFileFromCsv(BASE_PATH + "fileWithDuplicatedColumns.csv");

        assertThrows(FileParseException.class, () -> csvFileParser.parse(mockMultipartFileFromCsv));
    }

    @Test
    void parseFile() throws IOException {
        MockMultipartFile mockMultipartFileFromCsv = createMockMultipartFileFromCsv(BASE_PATH + "properAnimals.csv");
        ParsedFile parsedFileMock = ParsedFileMocker.mockParsedFileWithOneRecord(
                mockMultipartFileFromCsv.getOriginalFilename(), "code", "dog", "sort_order", "1", "type", "canine");

        ParsedFile parsedFile = csvFileParser.parse(mockMultipartFileFromCsv);

        assertTrue(new ReflectionEquals(parsedFileMock.content().records())
                .matches(parsedFile.content().records()));
        assertEquals(parsedFile.fileName(), parsedFileMock.fileName());
    }

    @Test
    void parseFiles() throws IOException {
        List<MultipartFile> multipartFilesMock = List.of(
                createMockMultipartFileFromCsv(BASE_PATH + "properAnimals.csv"),
                createMockMultipartFileFromCsv(BASE_PATH + "properCodes.csv"));
        List<ParsedFile> parsedFilesMock = List.of(
                ParsedFileMocker.mockParsedFileWithOneRecord(
                        multipartFilesMock.get(0).getOriginalFilename(),
                        "code",
                        "dog",
                        "sort_order",
                        "1",
                        "type",
                        "canine"),
                ParsedFileMocker.mockParsedFileWithOneRecord(
                        multipartFilesMock.get(1).getOriginalFilename(),
                        "code",
                        "canine",
                        "description",
                        "loyal companion"));

        List<ParsedFile> parsedFiles = csvFileParser.parseFiles(multipartFilesMock);
        IntStream.range(0, parsedFiles.size()).forEach(index -> {
            ParsedFile file = parsedFiles.get(index);
            ParsedFile fileMock = parsedFilesMock.get(index);
            assertTrue(new ReflectionEquals(fileMock.content().records())
                    .matches(file.content().records()));
            assertEquals(fileMock.fileName(), file.fileName());
        });
    }
}
