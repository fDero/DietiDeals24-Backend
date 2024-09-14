package exceptions;

public class LinkNotFoundException extends Exception {
    public LinkNotFoundException() {
        super("Link not found");
    }
}
