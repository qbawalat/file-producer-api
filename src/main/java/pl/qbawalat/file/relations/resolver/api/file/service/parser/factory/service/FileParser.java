package pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service;

import java.util.List;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileParseException;

public interface FileParser<InputFile, ParsedOutput> {
    List<ParsedOutput> parseFiles(List<InputFile> inputFiles) throws FileParseException;

    ParsedOutput parse(InputFile inputFile) throws FileParseException;
}
