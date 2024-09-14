package exceptions;

public class IbanNotYoursException extends Exception {
    public IbanNotYoursException() {
        super("IBAN is not yours");
    }
}
