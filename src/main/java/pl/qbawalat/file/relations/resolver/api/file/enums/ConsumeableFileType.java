package pl.qbawalat.file.relations.resolver.api.file.enums;

import java.util.Arrays;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import pl.qbawalat.file.relations.resolver.api.file.exception.ForbiddenFileTypeException;

@RequiredArgsConstructor
@Getter
public enum ConsumeableFileType {
    CSV("text/csv");

    private final String mimeType;

    public static ConsumeableFileType fromMimeType(String mimeType) {
        return Arrays.stream(ConsumeableFileType.values())
                .filter(fileType -> fileType.getMimeType().equalsIgnoreCase(mimeType))
                .findFirst()
                .orElseThrow(() -> new ForbiddenFileTypeException("Mime type not supported: " + mimeType));
    }
}
