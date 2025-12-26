package az.kapitalbank.mb.bff.transfermobile.exceptions;

public class AccountAlreadyExistsException extends RuntimeException {
    public AccountAlreadyExistsException(Long id) {
        super("Account already exists for customer id: " + id);
    }
}
