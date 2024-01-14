package pl.qbawalat.file.relations.resolver.api.file.service.merger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileRelationException;
import pl.qbawalat.file.relations.resolver.api.util.ParsedFileMocker;

@SpringBootTest
class FileMergerServiceTest {
    @Autowired
    private FileMergerService fileMerger;

    @Test
    void returnFileWhenOneFileProvided() {
        ParsedFile mainFile =
                ParsedFileMocker.mockParsedFileWithOneRecord("animals.csv", "code", "dog", "type", "canine");

        ParsedFile mergedFile = fileMerger.mergeFiles(mainFile, List.of());

        assertEquals(mainFile.fileName(), mergedFile.fileName());
        assertEquals(mainFile.content().records(), mergedFile.content().records());
    }

    @Test
    void mergeTwoRelatedFiles() {
        ParsedFile mainFile =
                ParsedFileMocker.mockParsedFileWithOneRecord("animals.csv", "code", "dog", "type", "canine");
        ParsedFile supplierFile = ParsedFileMocker.mockParsedFileWithOneRecord("types.csv", "code", "canine");
        HashMap<String, String> mergedCell = new HashMap<>();
        mergedCell.put("code", "canine");
        ParsedFile expectedResult =
                ParsedFileMocker.mockParsedFileWithOneRecord("animals.csv", "code", "dog", "type", mergedCell);

        ParsedFile mergedFile = fileMerger.mergeFiles(mainFile, List.of(supplierFile));

        assertEquals(expectedResult.fileName(), mergedFile.fileName());
        assertEquals(expectedResult.content().records(), mergedFile.content().records());
    }

    @Test
    void fileRelationExceptionWhenUnresolvableColumn() {
        ParsedFile mainFile =
                ParsedFileMocker.mockParsedFileWithOneRecord("animals.csv", "code", "dog", "type", "canine");
        ParsedFile supplierFile = ParsedFileMocker.mockParsedFileWithOneRecord("someCrazyNames.csv", "code", "canine");

        assertThrows(FileRelationException.class, () -> fileMerger.mergeFiles(mainFile, List.of(supplierFile)));
    }
}
