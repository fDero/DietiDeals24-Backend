package exceptions;

public class NoSuchPaymentMethodException extends Exception {
    public NoSuchPaymentMethodException() {
        super("payment method not found");
    }
}
