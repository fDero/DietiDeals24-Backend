package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Auction;
import entity.Bid;
import exceptions.AuctionNotActiveException;
import exceptions.BidOnYourOwnAuctionException;
import exceptions.NoSuchAuctionException;
import repository.AuctionRepository;
import repository.BidRepository;

@Service
public class BidsManagementService {
    
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final NotificationManagementService notificationManagementService;

    @Autowired
    BidsManagementService(
        AuctionRepository auctionRepository,
        BidRepository bidRepository,
        NotificationManagementService notificationManagementService
    ) {
        this.auctionRepository = auctionRepository;
        this.bidRepository = bidRepository;
        this.notificationManagementService = notificationManagementService;
    }

    public void validateBid(Bid bid, Auction auction) 
        throws
            NoSuchAuctionException,
            AuctionNotActiveException,
            BidOnYourOwnAuctionException
    {
        Integer userId = bid.getBidderId();
        if (auction.getCreatorId() == userId) {
            throw new BidOnYourOwnAuctionException();
        }
        if (!auction.getStatus().equals("active")) {
            throw new AuctionNotActiveException();
        }
    }

    public void saveBid(Bid bid) 
        throws
            NoSuchAuctionException,
            AuctionNotActiveException,
            BidOnYourOwnAuctionException
    {
        Integer auctionId = bid.getAuctionId();
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new NoSuchAuctionException());   
        validateBid(bid, auction);
        bidRepository.save(bid);
        notificationManagementService.notifyUserOfNewBid(bid, auction);
    }
}
