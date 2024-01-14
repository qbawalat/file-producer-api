package pl.qbawalat.file.relations.resolver.api.file.service.identifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;
import pl.qbawalat.file.relations.resolver.api.file.domain.RelatedFilesContainer;
import pl.qbawalat.file.relations.resolver.api.util.ParsedFileMocker;

@SpringBootTest
class FileIdentifierServiceTest {
    @Autowired
    private FileIdentifierService fileIdentifier;

    @Test
    void emptyParsedFilesListThrowsConstraintViolation() {
        List<ParsedFile> parsedFiles = List.of();

        assertThrows(ConstraintViolationException.class, () -> fileIdentifier.identifyFiles(parsedFiles));
    }

    @Test
    void identifyFiles() {
        ParsedFile mainFile =
                ParsedFileMocker.mockParsedFileWithOneRecord("animals.csv", "code", "dog", "type", "canine");
        ParsedFile supplierFile = ParsedFileMocker.mockParsedFileWithOneRecord("types.csv", "code", "canine");
        List<ParsedFile> parsedFiles = List.of(mainFile, supplierFile);

        RelatedFilesContainer relatedFilesContainer = fileIdentifier.identifyFiles(parsedFiles);

        assertEquals(
                mainFile.fileName(), relatedFilesContainer.mainFiles().get(0).fileName());
        assertEquals(
                supplierFile.fileName(),
                relatedFilesContainer.supplierFiles().get(0).fileName());
    }
}
