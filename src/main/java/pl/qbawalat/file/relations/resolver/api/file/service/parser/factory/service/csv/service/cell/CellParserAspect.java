package pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv.service.cell;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import pl.qbawalat.file.relations.resolver.api.common.logger.RequestScopedLogger;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedCell;

@Aspect
@Component
@RequiredArgsConstructor
public class CellParserAspect {

    private final RequestScopedLogger requestScopedLogger;

    @AfterReturning(
            pointcut =
                    "execution(* pl.qbawalat.file.relations.resolver.api.file.service.parser.factory.service.csv.service.cell.CellParserService.parseCell(..))",
            returning = "parsedCell")
    public void logMissingCellValueInfo(JoinPoint joinPoint, Optional<ParsedCell<String, String>> parsedCell) {
        String header = (String) joinPoint.getArgs()[0];
        parsedCell
                .map(ParsedCell::value)
                .filter(String::isEmpty)
                .ifPresent(value -> requestScopedLogger.log("Missing cell value of header: " + header));
    }
}
