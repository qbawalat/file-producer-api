package pl.qbawalat.file.relations.resolver.api.file.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.UNPROCESSABLE_ENTITY)
@Getter
public class FileRelationException extends BusinessLogicException {

    public FileRelationException(String message) {
        super(message);
    }
}
