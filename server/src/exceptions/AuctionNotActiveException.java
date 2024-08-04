package exceptions;

public class AuctionNotActiveException extends Exception {
        
    public AuctionNotActiveException() {
        super("Auction is not active.");
    }
}
