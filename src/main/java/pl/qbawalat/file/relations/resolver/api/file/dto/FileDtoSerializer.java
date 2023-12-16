package pl.qbawalat.file.relations.resolver.api.file.dto;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedRecord;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileDtoSerializerException;

public class FileDtoSerializer extends JsonSerializer<FileDto> {

    @Override
    public void serialize(FileDto fileDto, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeStartArray();
        fileDto.fileContent().forEach(parsedRecord -> writeObject(jsonGenerator, parsedRecord));
        jsonGenerator.writeEndArray();
    }

    private void writeObject(JsonGenerator jsonGenerator, ParsedRecord parsedRecord) {
        try {
            jsonGenerator.writeObject(parsedRecord);
        } catch (IOException e) {
            throw new FileDtoSerializerException("Error while serializing ParsedRecord", e);
        }
    }
}
