package pl.qbawalat.file.relations.resolver.api.file.service.relations.resolver.column;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import pl.qbawalat.file.relations.resolver.api.common.logger.RequestScopedLogger;
import pl.qbawalat.file.relations.resolver.api.file.domain.ParsedRecord;

@Aspect
@Component
@RequiredArgsConstructor
public class RelatedColumnResolverAspect {

    private final RequestScopedLogger requestScopedLogger;

    @Pointcut(
            "execution(* pl.qbawalat.file.relations.resolver.api.file.service.relations.resolver.column.RelatedColumnResolverService.resolveRelatedColumnValue(..))")
    public void resolveRelatedColumnValueMethod() {}

    @AfterReturning(value = "resolveRelatedColumnValueMethod()", returning = "resolvedRecord")
    public void logUnresolvableColumn(JoinPoint joinPoint, Optional<ParsedRecord> resolvedRecord) {
        Object relationColumnValue = joinPoint.getArgs()[1];

        if (resolvedRecord.isEmpty()) {
            requestScopedLogger.log("Tried to resolve nonexistent column value: " + relationColumnValue);
        }
    }
}
