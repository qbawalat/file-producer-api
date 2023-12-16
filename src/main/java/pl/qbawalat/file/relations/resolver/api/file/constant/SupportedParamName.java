package pl.qbawalat.file.relations.resolver.api.file.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SupportedParamName {
    public static final String FILE = "file";
    public static final String SORT = "sort";
    public static final String SANITIZE = "sanitize";
    public static final String PRODUCES = "produces"; // TODO use to resolve serializer
    public static final String CONSUMES = "consumes";
}
