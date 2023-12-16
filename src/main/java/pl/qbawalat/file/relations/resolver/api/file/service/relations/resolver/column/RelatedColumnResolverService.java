package pl.qbawalat.file.relations.resolver.api.file.service.relations.resolver.column;

import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedRecord;

@Service
@RequiredArgsConstructor
public class RelatedColumnResolverService {

    public Optional<ParsedRecord> resolveRelatedColumnValue(
            Map<String, ParsedRecord> supplierFileByCode, String relationColumnValue) {
        return Optional.ofNullable(supplierFileByCode.get(relationColumnValue));
    }
}
