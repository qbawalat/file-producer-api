package pl.qbawalat.file.relations.resolver.api.common.request;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pl.qbawalat.file.relations.resolver.api.file.constant.SupportedParamName;
import pl.qbawalat.file.relations.resolver.api.file.enums.ConsumeableFileType;

@Component
public class RequestParamsReader {

    public Optional<String> getRequestParameter(String param) {
        return getCurrentRequest().map(request -> request.getParameter(param));
    }

    public Optional<String> getFileType() {
        return getRequestParameter(SupportedParamName.CONSUMES)
                .map(fileType ->
                        ConsumeableFileType.valueOf(fileType.toUpperCase()).getMimeType());
    }

    private Optional<HttpServletRequest> getCurrentRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return Optional.ofNullable(attributes).map(ServletRequestAttributes::getRequest);
    }
}
