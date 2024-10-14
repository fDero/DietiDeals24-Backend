
package service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.Assert;
import entity.Auction;
import entity.Bid;
import repository.AuctionRepository;


import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import lombok.EqualsAndHashCode;

@ExtendWith(MockitoExtension.class)
class AuctionManagementServiceTest {
    
    @Mock
    private AuctionRepository mockAuctionRepository;

    @Mock
    private GeographicalAwarenessService mockGeographicalAwarenessService;

    @Mock
    private MetadataManagementService mockMetadataManagementService;

    @Mock 
    private PaymentProcessingService mockPaymentProcessingService;

    @EqualsAndHashCode(callSuper = false)
    static class ExampleMountainBikeAuction extends Auction {

        ExampleMountainBikeAuction(int numberOfBids, Integer currentBidderId, BigDecimal lowestBidSoFar, BigDecimal highestBidSoFar) {
            super.setMaximumBid(null);
            super.setMinimumBid(BigDecimal.valueOf(1200));
            super.setNumberOfBids(numberOfBids);
            super.setLowestBidSoFar(lowestBidSoFar);
            super.setHighestBidSoFar(highestBidSoFar);
            super.setCurrentBidderId(currentBidderId == null? null : BigDecimal.valueOf(currentBidderId.intValue()));
            super.setCreatorId(1);
            super.setCountry("IT");
            super.setCity("Rome");
            super.setItemCondition("new");
            super.setItemCategory("bikes");
            super.setMacroCategory("products");
            super.setStartTime(Timestamp.from(java.time.Instant.now()));
            super.setEndTime(Timestamp.from(java.time.Instant.now()));
            super.setItemName("Mountain Bike: Yeti SB150");
            super.setDescription("A beutiful mountain bike, perfect for enduro");
            super.setPicturesUrls(new String[] {"https://www.yeticycles.com"});
            super.setAuctionType("silent");
            super.setCurrency("EUR");
            super.setStatus("active");
            super.setId(1);
        }
    }

    @Test
    void t1() {
        AuctionManagementService auctionManagementService = new AuctionManagementService(
            mockAuctionRepository,
            mockGeographicalAwarenessService,
            mockMetadataManagementService
        );
        Integer newBidderId = 50;
        BigDecimal newBidAmount = BigDecimal.valueOf(1300);
        Auction auction = new ExampleMountainBikeAuction(0, null, null, null);
        Bid newBid = new Bid();
        newBid.setBidAmount(newBidAmount);
        newBid.setBidderId(newBidderId);
        newBid.setBidDate(Timestamp.from(java.time.Instant.now()));
        newBid.setPaymentInformations("<<credit-card-token>>");
        newBid.setAuctionId(auction.getId());
        Mockito.when(mockAuctionRepository.save(Mockito.any(Auction.class))).thenReturn(null);
        auctionManagementService.updateBidsRecord(auction,newBid);
        Auction expectedResult = new ExampleMountainBikeAuction(1, newBidderId, newBidAmount, newBidAmount);
        Assert.assertEquals(auction, expectedResult);
    }

    @Test
    void t2() {
        AuctionManagementService auctionManagementService = new AuctionManagementService(
            mockAuctionRepository,
            mockGeographicalAwarenessService,
            mockMetadataManagementService
        );
        Integer newBidderId = 60;
        BigDecimal oldLowestBidSoFar = BigDecimal.valueOf(1300);
        BigDecimal oldHighestBidSoFar = BigDecimal.valueOf(1900);
        BigDecimal newBidAmount = BigDecimal.valueOf(1400);
        Auction auction = new ExampleMountainBikeAuction(4, 50, oldLowestBidSoFar, oldHighestBidSoFar);
        Bid newBid = new Bid();
        newBid.setBidAmount(newBidAmount);
        newBid.setBidderId(newBidderId);
        newBid.setBidDate(Timestamp.from(java.time.Instant.now()));
        newBid.setPaymentInformations("<<credit-card-token>>");
        newBid.setAuctionId(auction.getId());
        Mockito.when(mockAuctionRepository.save(Mockito.any(Auction.class))).thenReturn(null);
        auctionManagementService.updateBidsRecord(auction,newBid);
        Auction expectedResult = new ExampleMountainBikeAuction(5, 50, oldLowestBidSoFar, oldHighestBidSoFar);
        Assert.assertEquals(auction, expectedResult);
    }

    @Test
    void t3() {
        AuctionManagementService auctionManagementService = new AuctionManagementService(
            mockAuctionRepository,
            mockGeographicalAwarenessService,
            mockMetadataManagementService
        );
        Integer newBidderId = 60;
        BigDecimal oldLowestBidSoFar = BigDecimal.valueOf(1300);
        BigDecimal oldHighestBidSoFar = BigDecimal.valueOf(1900);
        BigDecimal newBidAmount = BigDecimal.valueOf(1250);
        Auction auction = new ExampleMountainBikeAuction(4, 50, oldLowestBidSoFar, oldHighestBidSoFar);
        Bid newBid = new Bid();
        newBid.setBidAmount(newBidAmount);
        newBid.setBidderId(newBidderId);
        newBid.setBidDate(Timestamp.from(java.time.Instant.now()));
        newBid.setPaymentInformations("<<credit-card-token>>");
        newBid.setAuctionId(auction.getId());
        Mockito.when(mockAuctionRepository.save(Mockito.any(Auction.class))).thenReturn(null);
        auctionManagementService.updateBidsRecord(auction,newBid);
        Auction expectedResult = new ExampleMountainBikeAuction(5, 50, newBidAmount, oldHighestBidSoFar);
        Assert.assertEquals(auction, expectedResult);
    }
}
