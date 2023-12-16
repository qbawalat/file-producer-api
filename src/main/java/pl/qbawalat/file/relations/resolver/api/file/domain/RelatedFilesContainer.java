package pl.qbawalat.file.relations.resolver.api.file.domain;

import java.util.List;

public record RelatedFilesContainer(List<ParsedFile> mainFiles, List<ParsedFile> supplierFiles) {}
