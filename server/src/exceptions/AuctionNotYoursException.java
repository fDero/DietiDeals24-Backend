package exceptions;

public class AuctionNotYoursException extends Exception {
    public AuctionNotYoursException() {
        super("Auction not yours");
    }
}
