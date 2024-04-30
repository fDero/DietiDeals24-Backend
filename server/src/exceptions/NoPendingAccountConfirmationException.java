package exceptions;

public class NoPendingAccountConfirmationException extends Exception {
    public NoPendingAccountConfirmationException() {
        super("no pending account found for this email");
    }
}
