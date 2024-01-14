package pl.qbawalat.file.relations.resolver.api.file.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import pl.qbawalat.file.relations.resolver.api.common.request.RequestParamsReader;
import pl.qbawalat.file.relations.resolver.api.file.constraint.FileTypeConstraint;
import pl.qbawalat.file.relations.resolver.api.file.enums.ConsumeableFileType;

@Component
@RequiredArgsConstructor
public class MultipartFileValidator implements ConstraintValidator<FileTypeConstraint, MultipartFile> {
    private final RequestParamsReader requestParamsReader;

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        return isValidFileName(Objects.requireNonNull(multipartFile.getOriginalFilename()))
                && hasMatchingFileTypes(multipartFile);
    }

    private Boolean hasMatchingFileTypes(MultipartFile multipartFile) {
        return requestParamsReader
                .getFileType()
                .map(expectedFileType -> Arrays.stream(ConsumeableFileType.values())
                                .anyMatch(val -> val.getMimeType().equalsIgnoreCase(expectedFileType))
                        && expectedFileType.equalsIgnoreCase(multipartFile.getContentType()))
                .orElse(false);
    }

    private boolean isValidFileName(String input) {
        int lastDotIndex = input.lastIndexOf('.');
        return lastDotIndex > 0 && input.charAt(lastDotIndex - 1) == 's';
    }
}
