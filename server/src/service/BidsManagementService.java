package service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Auction;
import entity.Bid;
import exceptions.AuctionNotActiveException;
import exceptions.BidOnYourOwnAuctionException;
import exceptions.NoAccountWithSuchIdException;
import exceptions.NoSuchAuctionException;
import repository.AccountRepository;
import repository.AuctionRepository;
import repository.BidRepository;

@Service
public class BidsManagementService {
    
    private final AuctionManagementService auctionManagementService;
    private final AccountRepository accountRepository;
    private final BidRepository bidRepository;
    private final NotificationManagementService notificationManagementService;
    private final AuctionRepository auctionRepository;

    @Autowired
    BidsManagementService(
        AuctionManagementService auctionManagementService,
        AccountRepository accountRepository,
        AuctionRepository auctionRepository,
        BidRepository bidRepository,
        NotificationManagementService notificationManagementService
    ) {
        this.auctionManagementService = auctionManagementService;
        this.bidRepository = bidRepository;
        this.accountRepository = accountRepository;
        this.auctionRepository = auctionRepository;
        this.notificationManagementService = notificationManagementService;
    }

    public void validateBid(Bid bid, Auction auction) 
        throws
            AuctionNotActiveException,
            BidOnYourOwnAuctionException, 
            NoAccountWithSuchIdException, 
            NoSuchAuctionException
    {
        Integer bidderId = bid.getBidderId();
        Integer creatorId = auction.getCreatorId();
        if (bid.getBidAmount() == null || bid.getBidAmount().compareTo(BigDecimal.valueOf(0)) <= 0) {
            throw new IllegalArgumentException("Bid amount must be a non null positive amount");
        }
        if (auction.getMaximumBid() != null && bid.getBidAmount().compareTo(auction.getMaximumBid()) > 0) {
            throw new IllegalArgumentException("Bid amount must be lower then maximum amount allowed");
        }
        if (auction.getMinimumBid() != null && bid.getBidAmount().compareTo(auction.getMinimumBid()) < 0) {
            throw new IllegalArgumentException("Bid amount must be higher then minimum amount allowed");
        }
        if (!Objects.equals(auction.getId(), bid.getAuctionId())) {
            throw new InternalError();
        }
        if (!accountRepository.findById(bidderId).isPresent()) {
            throw new NoAccountWithSuchIdException();
        }
        if (!accountRepository.findById(creatorId).isPresent()) {
            throw new NoAccountWithSuchIdException();
        }
        if (!auctionRepository.findById(auction.getId()).isPresent()) {
            throw new NoSuchAuctionException();
        }
        if (!auctionRepository.findById(bid.getAuctionId()).isPresent()) {
            throw new NoSuchAuctionException();
        }
        if (!Objects.equals(auction.getId(), bid.getAuctionId())) {
            throw new NoSuchAuctionException();
        }
        if (Objects.equals(auction.getCreatorId(), bidderId)) {
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
            BidOnYourOwnAuctionException, 
            NoAccountWithSuchIdException
    {
        Integer auctionId = bid.getAuctionId();
        Auction auction = auctionManagementService.findById(auctionId);   
        validateBid(bid, auction);
        bidRepository.save(bid);
        notificationManagementService.notifyOldBidderOfBeingOutbid(auction);
        auctionManagementService.updateBidsRecord(auction, bid);
        notificationManagementService.notifyAuctionCreatorOfNewBid(auction);
    }

    public long countActiveBidsByBidderId(Integer bidderId) {
        return bidRepository.countOnlineBidsByBidderId(bidderId);
    }

    public long countPastBidsByBidderId(Integer bidderId) {
        return bidRepository.countPastBidsByBidderId(bidderId);
    }

    public List<Bid> fetchBidsByAuctionAndUser(Integer auctionId, Integer bidderId) {
        return bidRepository.findByBidderIdAndAuctionId(bidderId, auctionId);
    }
}
