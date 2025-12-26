package az.kapitalbank.mb.bff.transfermobile.globalExceptionHandler;

import az.kapitalbank.mb.bff.transfermobile.exceptions.AccountAlreadyExistsException;
import az.kapitalbank.mb.bff.transfermobile.exceptions.CustomerNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<?> handleCustomerNotFound(CustomerNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "error", ex.getMessage(),
                        "status", 404
                ));
    }

    @ExceptionHandler(AccountAlreadyExistsException.class)
    public ResponseEntity<?> handleAccountExists(AccountAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "error", ex.getMessage(),
                        "status", 409
                ));
    }
}