package pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv.service;

import static pl.qbawalat.file.relations.resolver.api.file.util.builder.CsvFormatBuilder.PIERCE_FILE_FORMAT;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import org.apache.commons.csv.CSVParser;
import org.springframework.stereotype.Service;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileParseException;

/**
 * The service caller is responsible for closing the returned Input Stream [CSVParser is io.Closeable].
 * This class performs Input Stream cleanup only if IOException happens.
 */
@Service
public class ParserCreatorService {
    public CSVParser createParser(InputStream inputStream) {
        Reader reader = new InputStreamReader(inputStream);
        try {
            return PIERCE_FILE_FORMAT.parse(reader);
        } catch (IOException | RuntimeException e) {
            cleanUpReaderStream(reader);
            throw new FileParseException("Error while reading file input stream. " + e.getMessage(), e);
        }
    }

    private void cleanUpReaderStream(Reader reader) {
        try {
            reader.close();
        } catch (IOException ex) {
            throw new FileParseException("Error while closing reader stream.", ex);
        }
    }
}
