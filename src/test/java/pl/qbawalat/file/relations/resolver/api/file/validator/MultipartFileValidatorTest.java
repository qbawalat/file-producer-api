package pl.qbawalat.file.relations.resolver.api.file.validator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

import jakarta.validation.ConstraintValidatorContext;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import pl.qbawalat.file.relations.resolver.api.common.request.RequestParamsReader;

class MultipartFileValidatorTest {

    @Mock
    private RequestParamsReader requestParamsReader;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @InjectMocks
    private MultipartFileValidator multipartFileValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void allowFileTypeMatchedToFileParam() {
        testFilesTypesComparison("text/csv", "text/csv", true);
    }

    @Test
    void rejectUnsupportedFileTypes() {
        testFilesTypesComparison("image/png", "image/png", false);
    }

    @Test
    void rejectMismatchedParamsFileTypes() {
        testFilesTypesComparison("text/csv", "image/png", false);
    }

    @Test
    void rejectMissingFileParam() {
        MultipartFile multipartFile = createMockMultipartFileWithHardcodedName("image/jpeg");

        when(requestParamsReader.getFileType()).thenReturn(Optional.empty());

        boolean result = multipartFileValidator.isValid(multipartFile, constraintValidatorContext);

        assertFalse(result);
        verify(requestParamsReader, times(1)).getFileType();
    }

    private void testFilesTypesComparison(String paramFileType, String actualFileType, boolean expectedResult) {
        MultipartFile multipartFile = createMockMultipartFileWithHardcodedName(actualFileType);
        when(requestParamsReader.getFileType()).thenReturn(Optional.of(paramFileType));

        boolean result = multipartFileValidator.isValid(multipartFile, constraintValidatorContext);

        assertEquals(expectedResult, result);
        verify(requestParamsReader, times(1)).getFileType();
    }

    private MultipartFile createMockMultipartFileWithHardcodedName(String contentType) {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getContentType()).thenReturn(contentType);
        when(multipartFile.getOriginalFilename()).thenReturn("animals.csv");
        return multipartFile;
    }
}
