package az.kapitalbank.mb.bff.transfermobile.exceptions;


import java.io.Serial;

public class CardNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public CardNotFoundException(String cardNumber) {
        super("Card not found: " + cardNumber);
    }
}