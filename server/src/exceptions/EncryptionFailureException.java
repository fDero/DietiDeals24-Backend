package exceptions;

public class EncryptionFailureException extends RuntimeException {

    public EncryptionFailureException() {
        super("encryption attempt failed");
    }
}
