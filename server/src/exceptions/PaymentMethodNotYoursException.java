package exceptions;

public class PaymentMethodNotYoursException extends Exception {
    public PaymentMethodNotYoursException() {
        super("payment-method is not yours");
    }
}
