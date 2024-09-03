package service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import entity.Auction;
import entity.Bid;
import exceptions.NoSuchAuctionException;
import repository.AuctionRepository;

@Service
public class AuctionManagementService {
    
    private final AuctionRepository auctionRepository;
    
    @Autowired
    public AuctionManagementService(
        AuctionRepository auctionRepository
    ) {
        this.auctionRepository = auctionRepository;
    }

    @Transactional
    public void updateStatuses() {
        auctionRepository.markAuctionsAsPending();
    }

    public Auction findById(Integer auctionId) 
        throws NoSuchAuctionException
    {
        return auctionRepository.findById(auctionId).orElseThrow(() -> new NoSuchAuctionException());
    }

    public void updateBidsRecord(Auction auction, Bid newBid) {
        if (auction.getHighestBidSoFar() == null || auction.getHighestBidSoFar().compareTo(newBid.getBidAmount()) < 0) {
            auction.setHighestBidSoFar(newBid.getBidAmount());
            if (auction.getAuctionType() == "silent") {
                auction.setCurrentBidderId(BigDecimal.valueOf(newBid.getBidderId()));
            }
        }
        if (auction.getLowestBidSoFar() == null || auction.getLowestBidSoFar().compareTo(newBid.getBidAmount()) > 0) {
            auction.setLowestBidSoFar(newBid.getBidAmount());
            if (auction.getAuctionType() == "reverse") {
                auction.setCurrentBidderId(BigDecimal.valueOf(newBid.getBidderId()));
            }
        }
        Integer oldNumberOfBids = auction.getNumberOfBids();
        Integer newNumberOfBids = oldNumberOfBids + 1;
        auction.setNumberOfBids(newNumberOfBids);
        auctionRepository.save(auction);
    }

    public long countOnlineAuctionsByCreatorId(Integer creatorId) {
        return auctionRepository.countOnlineAuctionsByCreatorId(creatorId);
    }

    public long countPastAuctionsByCreatorId(Integer creatorId) {
        return auctionRepository.countPastAuctionsByCreatorId(creatorId);
    }
}
