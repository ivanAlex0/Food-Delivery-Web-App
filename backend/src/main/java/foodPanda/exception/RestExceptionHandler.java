package foodPanda.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * The handler that catches exceptions in {@link foodPanda.service.impl} Implementations and returns {@link ResponseEntity} which contain useful messages in the form of {@link APIError}
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    protected ResponseEntity<APIError> handleInvalidCredentialsException(InvalidCredentialsException ex, WebRequest webRequest) {
        return new ResponseEntity<>(new APIError(HttpStatus.UNAUTHORIZED, ex, webRequest), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidInputException.class)
    protected ResponseEntity<APIError> handleInvalidInputException(InvalidInputException ex, WebRequest webRequest) {
        return new ResponseEntity<>(new APIError(HttpStatus.UNAUTHORIZED, ex, webRequest), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    protected ResponseEntity<APIError> handleRuntimeException(RuntimeException ex, WebRequest webRequest) {
        return new ResponseEntity<>(new APIError(HttpStatus.UNAUTHORIZED, ex, webRequest), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<APIError> handleRuntimeException(Exception ex, WebRequest webRequest) {
        return new ResponseEntity<>(new APIError(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED, ex, webRequest), HttpStatus.UNAUTHORIZED);
    }
}
