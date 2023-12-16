package pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv.service;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import pl.qbawalat.file.relations.resolver.api.common.request.RequestParamsReader;
import pl.qbawalat.file.relations.resolver.api.file.constant.SupportedParamName;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedCell;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedRecord;
import pl.qbawalat.file.relations.resolver.api.file.enums.SanitizationStrategy;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileParseException;
import pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv.service.cell.CellParserService;

@Service
@RequiredArgsConstructor
public class RecordParserService {

    private final CellParserService cellParser;
    private final RequestParamsReader requestParamsReader;

    public ParsedRecord parseRecord(List<String> headers, CSVRecord csvRecord) {
        List<String> cells = List.of(csvRecord.values());
        ParsedRecord parsedRecord = new ParsedRecord();
        SanitizationStrategy sanitizationStrategy = resolveSanitizationStrategy();
        for (int index = 0; index < cells.size(); index++) {
            Optional<ParsedCell<String, String>> parsedCell =
                    cellParser.parseCell(headers.get(index), cells.get(index), sanitizationStrategy);
            parsedCell.ifPresent(cell -> parsedRecord.put(cell.header(), cell.value()));
        }

        return parsedRecord;
    }

    private SanitizationStrategy resolveSanitizationStrategy() {

        return requestParamsReader
                .getRequestParameter(SupportedParamName.SANITIZE)
                .map(this::valueOfStrategy)
                .orElse(SanitizationStrategy.NO_SANITIZATION);
    }

    private SanitizationStrategy valueOfStrategy(String paramValue) {
        try {
            return SanitizationStrategy.valueOf(paramValue.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new FileParseException("SanitizationStrategy cannot be resolved for string: " + paramValue, e);
        }
    }
}
