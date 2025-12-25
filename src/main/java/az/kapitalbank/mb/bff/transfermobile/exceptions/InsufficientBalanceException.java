package az.kapitalbank.mb.bff.transfermobile.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    public InsufficientBalanceException(Long id) {
        super("Insufficient balance for customerId: " + id);
    }
}