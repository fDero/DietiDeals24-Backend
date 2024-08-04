package exceptions;

public class BidOnYourOwnAuctionException extends Exception {
        
    public BidOnYourOwnAuctionException() {
        super("can't bid on your own auction.");
    }
}
