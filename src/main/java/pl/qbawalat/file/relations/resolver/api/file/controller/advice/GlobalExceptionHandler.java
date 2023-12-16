package pl.qbawalat.file.relations.resolver.api.file.controller.advice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pl.qbawalat.file.relations.resolver.api.common.logger.RequestScopedLogger;
import pl.qbawalat.file.relations.resolver.api.file.domain.ResponseError;
import pl.qbawalat.file.relations.resolver.api.file.exception.BusinessLogicException;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final RequestScopedLogger requestScopedLogger;

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseError> handleException(RuntimeException ex) {
        requestScopedLogger.logError(ex.getMessage());
        return toResponseEntity("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MissingServletRequestParameterException.class})
    public ResponseEntity<ResponseError> handleException(MissingServletRequestParameterException ex) {
        return toResponseEntity("Missing required request parameter: " + ex.getParameterName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<ResponseError> handleException(MethodArgumentTypeMismatchException ex) {
        return toResponseEntity("Type mismatch of parameter: " + ex.getPropertyName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({BusinessLogicException.class})
    public ResponseEntity<ResponseError> handleException(BusinessLogicException ex) {
        return toResponseEntity(ex.getMessage(), ex.getResponseStatus());
    }

    private ResponseEntity<ResponseError> toResponseEntity(String message, HttpStatus httpStatus) {
        // TODO logging direct exception error messages is risky as message size may exceed default char column size
        requestScopedLogger.logError(message);
        return ResponseEntity.status(httpStatus).body(new ResponseError(message));
    }
}
