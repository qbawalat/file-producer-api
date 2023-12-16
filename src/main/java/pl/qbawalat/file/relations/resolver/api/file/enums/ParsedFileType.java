package pl.qbawalat.file.relations.resolver.api.file.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ParsedFileType {
    MAIN("mainFile"),
    SUPPLIER("supplierFile");

    private final String fileType;
}
