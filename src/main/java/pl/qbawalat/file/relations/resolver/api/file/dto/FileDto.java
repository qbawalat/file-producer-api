package pl.qbawalat.file.relations.resolver.api.file.dto;

import java.util.List;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedRecord;

public record FileDto(List<ParsedRecord> fileContent) {}
