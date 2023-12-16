package pl.qbawalat.file.relations.resolver.api.common.logger;

import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogPersister {

    private final LogRepository logRepository;
    private final RequestScopedLogger requestScopedLogger;

    public void saveLogsToDatabase() {
        List<String> logs = requestScopedLogger.getLogs();
        if (!logs.isEmpty()) {
            LogEntity logEntity = LogEntity.builder()
                    .messages(logs)
                    .timestamp(LocalDateTime.now())
                    .build();
            logRepository.save(logEntity);
            requestScopedLogger.clearLogs();
        }
    }
}
