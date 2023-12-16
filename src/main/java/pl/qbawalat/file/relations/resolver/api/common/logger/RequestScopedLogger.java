package pl.qbawalat.file.relations.resolver.api.common.logger;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
public class RequestScopedLogger {

    private final ThreadLocal<List<String>> logs = ThreadLocal.withInitial(ArrayList::new);
    private final Logger logger = LoggerFactory.getLogger(RequestScopedLogger.class);

    public void log(String message) {
        addMessageToLogs(message);
        logger.info("Message added to request logs: {}", message);
    }

    public void logError(String message) {
        addMessageToLogs(message);
        logger.error("Message added to request logs: {}", message);
    }

    private void addMessageToLogs(String message) {
        logs.get().add(message);
    }

    public List<String> getLogs() {
        return logs.get();
    }

    public void clearLogs() {
        logs.remove();
    }
}
