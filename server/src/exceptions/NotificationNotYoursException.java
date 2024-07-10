package exceptions;

public class NotificationNotYoursException extends Exception {
    public NotificationNotYoursException() {
        super("access denied: notification not yours");
    }
}
