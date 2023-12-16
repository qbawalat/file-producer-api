package pl.qbawalat.file.relations.resolver.api.file.controller.advice;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import pl.qbawalat.file.relations.resolver.api.common.logger.RequestScopedLogger;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileParseException;
import pl.qbawalat.file.relations.resolver.api.file.exception.FileRelationException;
import pl.qbawalat.file.relations.resolver.api.file.exception.ForbiddenFileTypeException;

class GlobalExceptionHandlerTest {
    @Mock
    private RequestScopedLogger requestScopedLogger;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void badRequestResponseForMissingParameterException() {
        MissingServletRequestParameterException exception =
                new MissingServletRequestParameterException("param", "string");

        assertEquals(
                HttpStatus.BAD_REQUEST,
                globalExceptionHandler.handleException(exception).getStatusCode());
        verify(requestScopedLogger, times(1)).logError("Missing required request parameter: param");
    }

    @Test
    void badRequestResponseForTypeMismatchException() {
        MethodArgumentTypeMismatchException exception =
                new MethodArgumentTypeMismatchException("value", String.class, "param", null, null);

        assertEquals(
                HttpStatus.BAD_REQUEST,
                globalExceptionHandler.handleException(exception).getStatusCode());
        verify(requestScopedLogger, times(1)).logError("Type mismatch of parameter: param");
    }

    @Test
    void unprocessableEntityStatusForFileParseException() {
        assertEquals(
                HttpStatus.UNPROCESSABLE_ENTITY,
                globalExceptionHandler
                        .handleException(new FileParseException("e"))
                        .getStatusCode());
    }

    @Test
    void unprocessableEntityStatusForFileRelationException() {
        assertEquals(
                HttpStatus.UNPROCESSABLE_ENTITY,
                globalExceptionHandler
                        .handleException(new FileRelationException("e"))
                        .getStatusCode());
    }

    @Test
    void unsupportedMediaTypeStatusForForbiddenFileTypeException() {
        assertEquals(
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                globalExceptionHandler
                        .handleException(new ForbiddenFileTypeException("e"))
                        .getStatusCode());
    }

    @Test
    void internalServerErrorResponseForRuntimeException() {
        assertEquals(
                HttpStatus.INTERNAL_SERVER_ERROR,
                globalExceptionHandler
                        .handleException(new RuntimeException("Test"))
                        .getStatusCode());
    }
}
