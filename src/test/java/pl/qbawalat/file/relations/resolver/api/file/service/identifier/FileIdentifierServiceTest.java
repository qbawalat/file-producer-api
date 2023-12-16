package pl.qbawalat.file.relations.resolver.api.file.service.identifier;

import static org.junit.jupiter.api.Assertions.assertThrows;

import jakarta.validation.ConstraintViolationException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;

@SpringBootTest
class FileIdentifierServiceTest {
    private static final String ID_HEADER = "code";
    private static final String MAIN_ID_VALUE = "dog";
    private static final String TYPES_SUPPLIER_ID_VALUE = "canine";
    private static final String TRICKS_SUPPLIER_ID_VALUE = "trick";

    @Autowired
    private FileIdentifierService fileIdentifier;

    @Test
    void emptyParsedFilesListThrowsConstraintViolation() {
        List<ParsedFile> parsedFiles = List.of();

        assertThrows(ConstraintViolationException.class, () -> fileIdentifier.identifyFiles(parsedFiles));
    }
}
