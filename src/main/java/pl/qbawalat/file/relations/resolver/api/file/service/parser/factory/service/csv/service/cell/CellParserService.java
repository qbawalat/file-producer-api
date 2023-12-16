package pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv.service.cell;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedCell;
import pl.qbawalat.file.relations.resolver.api.file.enums.SanitizationStrategy;
import pl.qbawalat.file.relations.resolver.api.file.predicate.ValidCellPredicate;

@Service
@RequiredArgsConstructor
public class CellParserService {

    private final ValidCellPredicate cellSanitizationPredicate;

    public Optional<ParsedCell<String, String>> parseCell(
            String header, String cell, SanitizationStrategy sanitizationStrategy) {
        return sanitizeCell(
                cellSanitizationPredicate.test(cell) ? sanitizationStrategy : SanitizationStrategy.NO_SANITIZATION,
                cell,
                header);
    }

    private Optional<ParsedCell<String, String>> sanitizeCell(
            SanitizationStrategy sanitizationStrategy, String cell, String header) {
        return switch (sanitizationStrategy) {
            case SKIP_EMPTY_CELLS -> Optional.empty();
            case EMPTY_CELLS_TO_NULLS -> Optional.of(new ParsedCell<>(header, null));
            case NO_SANITIZATION -> Optional.of(new ParsedCell<>(header, cell));
        };
    }
}
