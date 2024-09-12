package service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import entity.Auction;
import entity.Bid;
import exceptions.NoSuchAuctionException;
import repository.AuctionRepository;
import request.NewAuctionRequest;

@Service
public class AuctionManagementService {
    
    private final AuctionRepository auctionRepository;
    private final GeographicalAwarenessService geographicalAwarenessService;
    private final MetadataManagementService metadataManagementService;
    
    @Autowired
    public AuctionManagementService(
        AuctionRepository auctionRepository,
        GeographicalAwarenessService geographicalAwarenessService,
        MetadataManagementService metadataManagementService
    ) {
        this.auctionRepository = auctionRepository;
        this.geographicalAwarenessService = geographicalAwarenessService;
        this.metadataManagementService = metadataManagementService;
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
            if (auction.getAuctionType().equals("silent")) {
                auction.setCurrentBidderId(BigDecimal.valueOf(newBid.getBidderId()));
            }
        }
        if (auction.getLowestBidSoFar() == null || auction.getLowestBidSoFar().compareTo(newBid.getBidAmount()) > 0) {
            auction.setLowestBidSoFar(newBid.getBidAmount());
            if (auction.getAuctionType().equals("reverse")) {
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

    public void createNewAuction(Integer creatorId, NewAuctionRequest newAuctionRequest) {
        Auction newAuction = new Auction();
        Timestamp endTime = Timestamp.from(Instant.parse(newAuctionRequest.getEndTime()));
        validateNewAuction(newAuctionRequest);
        newAuction.setMaximumBid(newAuctionRequest.getMaximumBid());
        newAuction.setMinimumBid(newAuctionRequest.getMinimumBid());
        newAuction.setCreatorId(creatorId);
        newAuction.setCountry(newAuctionRequest.getCountry());
        newAuction.setCity(newAuctionRequest.getCity());
        newAuction.setItemCondition(newAuctionRequest.getItemCondition());
        newAuction.setItemCategory(newAuctionRequest.getItemCategory());
        newAuction.setMacroCategory(newAuctionRequest.getMacroCategory());
        newAuction.setStartTime(Timestamp.from(Instant.now()));
        newAuction.setEndTime(endTime);
        newAuction.setItemName(newAuctionRequest.getItemName());
        newAuction.setDescription(newAuctionRequest.getDescription());
        newAuction.setPicturesUrls(newAuctionRequest.getPicturesUrls());
        newAuction.setAuctionType(newAuctionRequest.getAuctionType());
        newAuction.setCurrency(newAuctionRequest.getCurrency());
        newAuction.setStatus("active");
        newAuction.setNumberOfBids(0);
        auctionRepository.save(newAuction);
    }

    private void validateNewAuction(NewAuctionRequest newAuctionRequest) {
        final BigDecimal minimumBid = newAuctionRequest.getMinimumBid();
        final BigDecimal maximumBid = newAuctionRequest.getMaximumBid();
        final Timestamp endTime = Timestamp.from(Instant.parse(newAuctionRequest.getEndTime()));
        final String country = newAuctionRequest.getCountry();
        final String city = newAuctionRequest.getCity();
        final boolean reverse = newAuctionRequest.getAuctionType().equals("reverse");
        final boolean silent = newAuctionRequest.getAuctionType().equals("silent");
        final String category = newAuctionRequest.getItemCategory();
        final String macroCategory = newAuctionRequest.getMacroCategory();
        if (endTime == null || endTime.before(new Timestamp(System.currentTimeMillis()))) {
            throw new IllegalArgumentException("Auction end time must be in the future");
        }
        if (maximumBid != null && maximumBid.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException("Minimum bid must be positive");
        }
        if (minimumBid != null && minimumBid.compareTo(new BigDecimal(0)) < 0) {
            throw new IllegalArgumentException("Maximum bid must be positive");
        }
        if (minimumBid != null && maximumBid != null && minimumBid.compareTo(maximumBid) > 0) {
            throw new IllegalArgumentException("Minimum bid must be less than maximum bid");
        }
        if (!geographicalAwarenessService.checkThatCityBelogsToCountry(country, city)) {
            throw new IllegalArgumentException("City does not belong to country");
        }
        if (!reverse && !silent) {
            throw new IllegalArgumentException("Invalid auction type");
        }
        if (category != null && macroCategory != null && !metadataManagementService.checkThatCategoryIsMacrocategory(category, macroCategory)) {
            throw new IllegalArgumentException("Invalid category");
        }
        if (category == null) {
            throw new IllegalArgumentException("Category must be specified");
        }
        if (category != null && macroCategory == null) {
            String inferredMacroCategory = metadataManagementService.getMacroCategoryForCategory(category);
            newAuctionRequest.setMacroCategory(inferredMacroCategory);
        }
    }
}
