package service;

public class InvalidAuctionFinalizationChoiceException extends Exception {
    public InvalidAuctionFinalizationChoiceException() {
        super("Invalid auction finalization choice");
    }
}
