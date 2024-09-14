package exceptions;

public class IbanNotFoundException extends Exception {
    public IbanNotFoundException() {
        super("IBAN not found");
    }
}
