package exceptions;

public class UnrecognizedCountryException extends Exception {
    public UnrecognizedCountryException() {
        super("no such country (please use ISO 3166-1 country codes)");
    }
}
