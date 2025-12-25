package az.kapitalbank.mb.bff.transfermobile.exceptions;

public class InvalidAmountException extends RuntimeException {
    public InvalidAmountException() {
        super("Amount must be greater than zero");
    }
}