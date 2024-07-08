package exceptions;

public class NoAuctionWithSuchIdException extends Exception {
    public NoAuctionWithSuchIdException() {
        super("no auction with such id");
    }
}