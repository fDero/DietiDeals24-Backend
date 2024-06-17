package exceptions;

public class TooManyConfirmationCodes extends Exception {
    public TooManyConfirmationCodes() {
        super("too many errors, please try again from scratch");
    }
}
