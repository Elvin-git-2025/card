package az.kapitalbank.mb.bff.transfermobile.globalExceptionHandler;

import az.kapitalbank.mb.bff.transfermobile.exceptions.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(AccountNotFoundException ex) {
        return Map.of("error", ex.getMessage());
    }
}