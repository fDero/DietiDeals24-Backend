package exceptions;

public class WrongConfirmationCodeException extends Exception {
    public WrongConfirmationCodeException() {
        super("invalid confirmation code");
    }
}
