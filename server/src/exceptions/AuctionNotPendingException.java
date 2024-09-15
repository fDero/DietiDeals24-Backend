package exceptions;

public class AuctionNotPendingException extends Exception {
        
    public AuctionNotPendingException() {
        super("Auction is not active.");
    }
}
