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
        BigDecimal highestBidBeforeCurrent = auction.getHighestBidSoFar();
        BigDecimal lowestBidBeforeCurrent = auction.getLowestBidSoFar();
        auction.setHighestBidSoFar(highestBidBeforeCurrent.max(newBid.getBidAmount()));
        auction.setLowestBidSoFar(lowestBidBeforeCurrent.min(newBid.getBidAmount()));
        if (auction.getAuctionType() == "reverse") {
            if (auction.getHighestBidSoFar().equals(newBid.getBidAmount())) {
                auction.setCurrentBidderId(newBid.getBidderId());
            }
        }
        else {
            assert (auction.getAuctionType() == "silent");
            if (auction.getLowestBidSoFar().equals(newBid.getBidAmount())) {
                auction.setCurrentBidderId(newBid.getBidderId());
            }
        }
        Integer oldNumberOfBids = auction.getNumberOfBids();
        Integer newNumberOfBids = oldNumberOfBids + 1;
        auction.setNumberOfBids(newNumberOfBids);
        auctionRepository.save(auction);
    }
}
