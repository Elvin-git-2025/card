package az.kapitalbank.mb.bff.transfermobile.transfer.exceptions;

public class InvalidTransferException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidTransferException(String message) {
        super(message);
    }
}
