package ru.practicum.shareit.exeption.handler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exeption.AlreadyExistsException;
import ru.practicum.shareit.exeption.IllegalEnumException;
import ru.practicum.shareit.exeption.NotFoundException;
import ru.practicum.shareit.exeption.ValidationException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    protected ResponseEntity<Object> handleNotFoundEx(RuntimeException ex, WebRequest request) {
        String message = "Object not found";
        return getResponseEntity(message, ex, NOT_FOUND, request);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    protected ResponseEntity<Object> handleAlreadyExistsEx(RuntimeException ex, WebRequest request) {
        String message = "Already exists";
        return getResponseEntity(message, ex, CONFLICT, request);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIncorrectParameterEx(RuntimeException ex, WebRequest request) {
        String message = "Invalid parameter value";
        return getResponseEntity(message, ex, BAD_REQUEST, request);
    }

    @ExceptionHandler({ValidationException.class, ConstraintViolationException.class})
    protected ResponseEntity<Object> handleValidateEx(RuntimeException ex, WebRequest request) {
        String message = "Incorrect data";
        return getResponseEntity(message, ex, BAD_REQUEST, request);
    }

    @ExceptionHandler(IllegalEnumException.class)
    public ResponseEntity<Map<String, String>> handleEnumEx(IllegalEnumException ex) {
        return new ResponseEntity<>(Map.of(
                "error", ex.getMessage(),
                "emotions", "A little humor: I am shocked by the testers"),
                HttpStatus.BAD_REQUEST);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(@NonNull MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        String message = "Incorrect data";
        return getResponseEntity(message, ex, status, request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(@NonNull HttpMessageNotReadableException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        String message = "Unreadable JSON";
        return getResponseEntity(message, ex, status, request);
    }

    @NonNull
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(@NonNull NoHandlerFoundException ex,
                                                                   @NonNull HttpHeaders headers,
                                                                   @NonNull HttpStatus status,
                                                                   @NonNull WebRequest request) {
        String message = "The handler for the endpoint was not found";
        return getResponseEntity(message, ex, status, request);
    }

    @ExceptionHandler
    protected ResponseEntity<Object> handleThrowableEx(Throwable ex, WebRequest request) {
        String message = "Unexpected error";
        return getResponseEntity(message, ex, INTERNAL_SERVER_ERROR, request);
    }

    private ResponseEntity<Object> getResponseEntity(String message,
                                                     Throwable ex,
                                                     HttpStatus status,
                                                     WebRequest request) {

        log.error(message + ": {}", ex.getMessage(), ex);
        ErrorResponse errorResponse = getNewBody(message, status, request, ex);
        return new ResponseEntity<>(errorResponse, status);
    }

    private ErrorResponse getNewBody(String message, HttpStatus status, WebRequest request, Throwable ex) {
        List<String> reasons;
        if (ex instanceof BindException) {
            reasons = ((BindException) ex)
                    .getAllErrors()
                    .stream()
                    .map(this::getErrorString)
                    .collect(Collectors.toList());
        } else {
            reasons = Arrays.stream(ex.getMessage().split(", ")).collect(Collectors.toList());
        }
        return ErrorResponse.builder()
                .status(status.value())
                .endPoint(getRequestURI(request))
                .detailMessage(message)
                .error(status.getReasonPhrase())
                .reasons(reasons)
                .build();
    }

    private String getErrorString(ObjectError error) {
        if (error instanceof FieldError) {
            return ((FieldError) error).getField() + ' ' + error.getDefaultMessage();
        }
        return error.getDefaultMessage();
    }

    private String getRequestURI(WebRequest request) {
        if (request instanceof ServletWebRequest) {
            HttpServletRequest requestHttp = ((ServletWebRequest) request).getRequest();
            return String.format("%s %s", requestHttp.getMethod(), requestHttp.getRequestURI());
        } else {
            return "";
        }
    }
}