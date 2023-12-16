package pl.qbawalat.file.relations.resolver.api.file.domain;

import java.util.List;
import pl.qbawalat.file.relations.resolver.api.file.enums.ParsedFileType;

public record ResolvedRelationFile(String fileName, ParsedFileType fileType, List<ParsedRecord> entries) {}
