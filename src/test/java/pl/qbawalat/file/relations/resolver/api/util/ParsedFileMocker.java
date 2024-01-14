package pl.qbawalat.file.relations.resolver.api.util;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFile;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedFileContent;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedRecord;

public class ParsedFileMocker {

    private ParsedFileMocker() {}

    public static ParsedFile mockParsedFile(String fileName, ParsedRecord... parsedRecords) {
        List<ParsedRecord> records = Arrays.stream(parsedRecords).toList();
        ParsedFileContent parsedFileContent = mock();
        when(parsedFileContent.records()).thenReturn(records);
        ParsedFile parsedFile = mock();
        when(parsedFile.fileName()).thenReturn(fileName);
        when(parsedFile.content()).thenReturn(parsedFileContent);
        return parsedFile;
    }

    public static ParsedFile mockParsedFileWithOneRecord(String fileName, Object... entries) {
        return mockParsedFile(fileName, mockParsedRecords(entries));
    }

    public static ParsedRecord mockParsedRecords(Object... entries) {
        if (entries.length % 2 != 0) {
            throw new IllegalArgumentException("Entries must be provided in key-value pairs");
        }
        ParsedRecord parsedRecord = new ParsedRecord();
        for (int iterator = 0; iterator < entries.length; iterator += 2) {
            parsedRecord.put((String) entries[iterator], entries[iterator + 1]);
        }
        return parsedRecord;
    }
}
