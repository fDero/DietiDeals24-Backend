package exceptions;

public class NoSuchAuctionException extends Exception {
        
    public NoSuchAuctionException() {
        super("No such auction found.");
    }
}
