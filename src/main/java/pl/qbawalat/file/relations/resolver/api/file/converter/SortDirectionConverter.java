package pl.qbawalat.file.relations.resolver.api.file.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.domain.Sort;

public class SortDirectionConverter implements Converter<String, Sort.Direction> {
    @Override
    public Sort.Direction convert(String source) {
        return Sort.Direction.fromString(source.toUpperCase());
    }
}
