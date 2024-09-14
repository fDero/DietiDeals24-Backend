package exceptions;

public class LinkNotYoursException extends Exception {
    public LinkNotYoursException() {
        super("Link is not yours");
    }
}
