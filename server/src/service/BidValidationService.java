package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Auction;
import entity.Bid;
import exceptions.AuctionNotActiveException;
import exceptions.BidOnYourOwnAuctionException;
import exceptions.NoSuchAuctionException;
import repository.AuctionRepository;

@Service
public class BidValidationService {
    
    private final AuctionRepository auctionRepository;

    @Autowired
    BidValidationService(
        AuctionRepository auctionRepository
    ) {
        this.auctionRepository = auctionRepository;
    }

    public void validateBid(Bid bid) 
        throws
            NoSuchAuctionException,
            AuctionNotActiveException,
            BidOnYourOwnAuctionException
    {
        Integer userId = bid.getBidderId();
        Integer auctionId = bid.getAuctionId();
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new NoSuchAuctionException());
        if (auction.getCreatorId() == userId) {
            throw new BidOnYourOwnAuctionException();
        }
        if (!auction.getStatus().equals("active")) {
            throw new AuctionNotActiveException();
        }
    }
}
