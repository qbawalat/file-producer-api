package pl.qbawalat.file.relations.resolver.api.file.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ColumnHeader {
    SORT_ORDER("sort_order"),
    CODE("code");

    private final String value;
}
