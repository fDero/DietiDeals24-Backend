package exceptions;

public class InvalidPictureFileExtension extends Exception {

    public InvalidPictureFileExtension(){
        super("Only 'png','jpeg','webp' are allowed");
    }
}
