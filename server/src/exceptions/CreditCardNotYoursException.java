package exceptions;

public class CreditCardNotYoursException extends Exception {
    public CreditCardNotYoursException() {
        super("Credit card is not yours");
    }
}
