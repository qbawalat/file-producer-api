package pl.qbawalat.file.relations.resolver.api.file.service.sorter;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.qbawalat.file.relations.resolver.api.common.request.RequestParamsReader;
import pl.qbawalat.file.relations.resolver.api.file.constant.SupportedParamName;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedRecord;
import pl.qbawalat.file.relations.resolver.api.file.enums.ColumnHeader;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileParseException;

@Service
@RequiredArgsConstructor
public class FileRecordsSorterService {
    private final RequestParamsReader requestParamsReader;

    public List<ParsedRecord> sortRecords(List<ParsedRecord> records) {
        Optional<String> sortType = requestParamsReader.getRequestParameter(SupportedParamName.SORT);
        return sortType.map(type -> records.stream()
                        .sorted(resolveComparator(Sort.Direction.fromString(type)))
                        .toList())
                .orElse(records);
    }

    private Comparator<ParsedRecord> resolveComparator(Sort.Direction sortDirection) {
        Comparator<ParsedRecord> comparator = Comparator.comparingInt(this::resolveSortOrderValue);
        return sortDirection.isAscending() ? comparator : comparator.reversed();
    }

    private int resolveSortOrderValue(ParsedRecord entry) {
        Object sortColumnValue = entry.get(ColumnHeader.SORT_ORDER.getValue());
        try {
            if (sortColumnValue instanceof String string && !string.isEmpty()) {
                return Integer.parseInt(string);
            } else {
                throw new FileParseException(
                        "Sort column value must be of String type: " + ColumnHeader.SORT_ORDER.getValue());
            }
        } catch (NumberFormatException numberFormatException) {
            throw new FileParseException("Sort column value must be a numeric String: " + sortColumnValue.toString());
        }
    }
}
