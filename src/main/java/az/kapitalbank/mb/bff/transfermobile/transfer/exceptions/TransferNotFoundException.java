package az.kapitalbank.mb.bff.transfermobile.transfer.exceptions;

public class TransferNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TransferNotFoundException(Long id) {
        super("Could not find transfer with id :" + id);
    }
}
