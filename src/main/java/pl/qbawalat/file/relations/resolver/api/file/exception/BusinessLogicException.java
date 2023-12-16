package pl.qbawalat.file.relations.resolver.api.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
public abstract class BusinessLogicException extends RuntimeException {
    protected BusinessLogicException(String message, Throwable cause) {
        super(message, cause);
    }

    protected BusinessLogicException(String message) {
        super(message);
    }

    public HttpStatus getResponseStatus() {
        ResponseStatus responseStatus = getClass().getAnnotation(ResponseStatus.class);
        return responseStatus.code();
    }
}
