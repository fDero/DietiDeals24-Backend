package exceptions;

public class UnrecognizedCityException extends Exception {
    public UnrecognizedCityException() {
        super("no such city");
    }
}
