package exceptions;

public class NoSuchNotificationException extends Exception {
    public NoSuchNotificationException() {
        super("access denied: no such notification");
    }
}
