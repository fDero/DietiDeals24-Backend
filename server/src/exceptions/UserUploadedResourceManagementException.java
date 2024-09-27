package exceptions;

public class UserUploadedResourceManagementException extends RuntimeException {

    public UserUploadedResourceManagementException() {
        super("Error managing an user uploaded resource");
    }
}
