package az.kapitalbank.mb.bff.transfermobile.exceptions;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) {
        super("Account not found for customerId: " + id);
    }
}