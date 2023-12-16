package pl.qbawalat.file.relations.resolver.api.file.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.qbawalat.file.relations.resolver.api.file.dto.FileDto;
import pl.qbawalat.file.relations.resolver.api.file.dto.FileDtoSerializer;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule().addSerializer(FileDto.class, new FileDtoSerializer()));
        return objectMapper;
    }
}
