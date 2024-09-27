package exceptions;

public class JsonEncodingDecodingException extends RuntimeException {

    public JsonEncodingDecodingException(Object object, Exception exception){
        super(
            "can't encode object because not json serializable: " +
            "source=" + object + " error=" + exception.getMessage()
        );
    }
}
