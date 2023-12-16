package pl.qbawalat.file.relations.resolver.api.file.util.builder;

import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVFormat;

@UtilityClass
public class CsvFormatBuilder {
    // TODO layer to differentiate file formats
    public static final CSVFormat PIERCE_FILE_FORMAT = CSVFormat.DEFAULT
            .builder()
            .setDelimiter(Delimiter.CSV)
            .setHeader()
            .setSkipHeaderRecord(true)
            .build();
}
