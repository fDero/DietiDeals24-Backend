package exceptions;

public class GeographicalAwarenessFailureException extends Exception {

    public GeographicalAwarenessFailureException() {
        super("Error while fetching european countries or cities");
    }
}
