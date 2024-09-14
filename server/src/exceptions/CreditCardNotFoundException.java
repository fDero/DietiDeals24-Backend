package exceptions;

public class CreditCardNotFoundException extends Exception {
    public CreditCardNotFoundException() {
        super("Credit card not found");
    }
}
