package az.kapitalbank.mb.bff.transfermobile.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException(String cardNumber) {
        super("Card not found: " + cardNumber);
    }
}