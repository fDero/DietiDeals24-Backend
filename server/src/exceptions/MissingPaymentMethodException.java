package exceptions;

public class MissingPaymentMethodException extends Exception {
    public MissingPaymentMethodException() {
        super("Payment method missing from request.");
    }
    
}
