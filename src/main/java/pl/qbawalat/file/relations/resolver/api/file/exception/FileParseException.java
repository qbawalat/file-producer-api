package pl.qbawalat.file.relations.resolver.api.file.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
@Getter
public class FileParseException extends BusinessLogicException {

    public FileParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileParseException(String message) {
        super(message);
    }
}
