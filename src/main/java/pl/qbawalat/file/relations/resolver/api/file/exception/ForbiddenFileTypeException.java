package pl.qbawalat.file.relations.resolver.api.file.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class ForbiddenFileTypeException extends BusinessLogicException {

    public ForbiddenFileTypeException(String message) {
        super(message);
    }
}
