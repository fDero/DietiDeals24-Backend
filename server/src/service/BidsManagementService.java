package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Auction;
import entity.Bid;
import exceptions.AuctionNotActiveException;
import exceptions.BidOnYourOwnAuctionException;
import exceptions.NoSuchAuctionException;
import repository.BidRepository;

@Service
public class BidsManagementService {
    
    private final AuctionManagementService auctionManagementService;
    private final BidRepository bidRepository;
    private final NotificationManagementService notificationManagementService;

    @Autowired
    BidsManagementService(
        AuctionManagementService auctionManagementService,
        BidRepository bidRepository,
        NotificationManagementService notificationManagementService
    ) {
        this.auctionManagementService = auctionManagementService;
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
        Auction auction = auctionManagementService.findById(auctionId);   
        validateBid(bid, auction);
        bidRepository.save(bid);
        notificationManagementService.notifyOldBidderOfBeingOutbid(bid, auction);
        auctionManagementService.updateBidsRecord(auction, bid);
        notificationManagementService.notifyAuctionCreatorOfNewBid(bid, auction);
    }
}
