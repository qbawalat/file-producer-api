package pl.qbawalat.file.relations.resolver.api.file.predicate;

import java.util.function.Predicate;
import org.springframework.stereotype.Component;

@Component
public class ValidCellPredicate implements Predicate<String> {

    @Override
    public boolean test(String cell) {
        return cell.isEmpty();
    }
}
