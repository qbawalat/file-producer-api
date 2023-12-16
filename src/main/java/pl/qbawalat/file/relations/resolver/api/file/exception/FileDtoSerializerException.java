package pl.qbawalat.file.relations.resolver.api.file.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class FileDtoSerializerException extends BusinessLogicException {
    public FileDtoSerializerException(String message, Throwable cause) {
        super(message, cause);
    }
}
