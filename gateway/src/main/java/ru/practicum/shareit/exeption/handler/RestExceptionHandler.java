package ru.practicum.shareit.exeption.handler;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.practicum.shareit.exeption.IllegalEnumException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    protected ResponseEntity<Object> handleValidateEx(RuntimeException ex, WebRequest request) {
        String message = "Incorrect data";
        return getResponseEntity(message, ex, BAD_REQUEST, request);
    }

    @ExceptionHandler(IllegalEnumException.class)
    public ResponseEntity<Map<String, String>> handleEnumEx(IllegalEnumException ex) {
        return new ResponseEntity<>(Map.of("error", ex.getMessage()), BAD_REQUEST);
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