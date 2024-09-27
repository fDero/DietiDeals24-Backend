package exceptions;

public class GeographicalAwarenessFailureException extends RuntimeException {

    public GeographicalAwarenessFailureException() {
        super("Error while fetching european countries or cities");
    }
}
