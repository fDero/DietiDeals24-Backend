
package service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Optional;

import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testng.Assert;
import org.junit.jupiter.api.Assertions;

import entity.Account;
import entity.Auction;
import entity.Bid;
import exceptions.NoAccountWithSuchIdException;
import exceptions.NoSuchAuctionException;
import repository.AccountRepository;
import repository.AuctionRepository;
import repository.BidRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import lombok.EqualsAndHashCode;

@ExtendWith(MockitoExtension.class)
class BidsManagementServiceTest {
    
    @Mock
    private AuctionManagementService mockAuctionManagementService;

    @Mock
    private BidRepository mockBidRepository;

    @Mock 
    private AuctionRepository mockAuctionRepository;

    @Mock
    private AccountRepository mockAccountRepository;

    @Mock
    private NotificationManagementService mockNotificationManagementService;

    enum AuctionType {
        SILENT,
        REVERSE
    }

    @EqualsAndHashCode(callSuper = false)
    static class ExampleMountainBikeAuction extends Auction {

        ExampleMountainBikeAuction(Integer auctionId, Integer creatorId, BigDecimal maximumBid, BigDecimal minimumBid, AuctionType type) {
            super.setMaximumBid(maximumBid);
            super.setMinimumBid(minimumBid);
            super.setNumberOfBids(0);
            super.setLowestBidSoFar(null);
            super.setHighestBidSoFar(null);
            super.setCurrentBidderId(null);
            super.setCreatorId(creatorId);
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
            super.setAuctionType(type.name().toLowerCase());
            super.setCurrency("EUR");
            super.setStatus("active");
            super.setId(auctionId);
        }

        ExampleMountainBikeAuction(Integer auctionId, Integer creatorId, Integer maximumBid, Integer minimumBid, AuctionType type) {
            this(
                auctionId,
                creatorId, 
                maximumBid != null ? new BigDecimal(maximumBid) : null, 
                minimumBid != null ? new BigDecimal(minimumBid) : null, 
                type
            );
        }
    }

    @EqualsAndHashCode(callSuper = false)
    static class ExampleMountainBikeBid extends Bid {

        ExampleMountainBikeBid(Integer auctionId, Integer bidderId, BigDecimal bidAmount) {
            super.setBidDate(Timestamp.from(java.time.Instant.now()));
            super.setPaymentInformations("<<demo-payment-informations>>");
            super.setBidderId(bidderId);
            super.setAuctionId(auctionId);
            super.setBidAmount(bidAmount);
            super.setId(1);
        }

        ExampleMountainBikeBid(Integer auctionId, Integer bidderId, int bidAmount) {
            this(auctionId, bidderId, new BigDecimal(bidAmount));
        }
    }

    @EqualsAndHashCode(callSuper = false)
    static class ExampleAccount extends Account {

        ExampleAccount(String name, String surname, Integer id) {
            super.setId(id);
            super.setName(name);
            super.setSurname(surname);
            super.setUsername(name + surname);
            super.setEmail(name + "." + surname + "@example.com");
            super.setCountry("ENG");
            super.setCity("London");
            super.setBirthday(Timestamp.from(java.time.Instant.now()));
            super.setLastLogin(Timestamp.from(java.time.Instant.now()));
            super.setAccountCreation(Timestamp.from(java.time.Instant.now()));
            super.setAccountProvider("TEST");
        }
    }

    @Test
    void t1() {
        BidsManagementService bidsManagementService = new BidsManagementService(
            mockAuctionManagementService,
            mockAccountRepository,
            mockAuctionRepository,
            mockBidRepository,
            mockNotificationManagementService
        );
        Account auctionCreator = new ExampleAccount("Roger", "Penske", 1);
        Account bidder = new ExampleAccount("Reinhold", "Joest", 2);        
        Auction auction = new ExampleMountainBikeAuction(
            1, 
            auctionCreator.getId(), 
            null, 
            500, 
            AuctionType.SILENT
        );
        Mockito.when(mockAccountRepository.findById(Mockito.any(Integer.class)))
            .thenAnswer(invocation -> {
                Integer id = invocation.getArgument(0);
                return switch (id) {
                    case null -> Optional.empty();
                    case Integer i when Objects.equals(i, bidder.getId()) -> Optional.of(bidder);
                    case Integer i when Objects.equals(i, auctionCreator.getId()) -> Optional.of(auctionCreator);
                    default -> Optional.empty();
                };
            }
        );
        Mockito.when(mockAuctionRepository.findById(auction.getId())).thenReturn(Optional.of(auction));
        Bid bid = new ExampleMountainBikeBid(auction.getId(), bidder.getId(), 700);
        Assertions.assertDoesNotThrow(() -> {
            bidsManagementService.validateBid(bid, auction);
        });
    }

    @Test
    void t2() {
        BidsManagementService bidsManagementService = new BidsManagementService(
            mockAuctionManagementService,
            mockAccountRepository,
            mockAuctionRepository,
            mockBidRepository,
            mockNotificationManagementService
        );
        Account auctionCreator = new ExampleAccount("Roger", "Penske", 1);
        Account bidder = new ExampleAccount("Reinhold", "Joest", 2);        
        Auction auction = new ExampleMountainBikeAuction(
            1, 
            auctionCreator.getId(), 
            1000, 
            null, 
            AuctionType.REVERSE
        );
        Mockito.when(mockAccountRepository.findById(Mockito.any(Integer.class)))
            .thenAnswer(invocation -> {
                Integer id = invocation.getArgument(0);
                return switch (id) {
                    case null -> Optional.empty();
                    case Integer i when Objects.equals(i, bidder.getId()) -> Optional.of(bidder);
                    case Integer i when Objects.equals(i, auctionCreator.getId()) -> Optional.of(auctionCreator);
                    default -> Optional.empty();
                };
            }
        );
        Mockito.when(mockAuctionRepository.findById(auction.getId())).thenReturn(Optional.of(auction));
        Bid bid = new ExampleMountainBikeBid(null, bidder.getId(), 700);
        Assert.assertThrows(NoSuchAuctionException.class, () -> {
            bidsManagementService.validateBid(bid, auction);
        });
    }

    @Test
    void t3() {
        BidsManagementService bidsManagementService = new BidsManagementService(
            mockAuctionManagementService,
            mockAccountRepository,
            mockAuctionRepository,
            mockBidRepository,
            mockNotificationManagementService
        );
        Account auctionCreator = new ExampleAccount("Roger", "Penske", 1);
        Auction auction = new ExampleMountainBikeAuction(
            1, 
            auctionCreator.getId(), 
            null, 
            500, 
            AuctionType.SILENT
        );        
        Bid bid = new ExampleMountainBikeBid(auction.getId(), null, 700);
        Mockito.when(mockAccountRepository.findById(null)).thenReturn(Optional.empty());
        Assert.assertThrows(NoAccountWithSuchIdException.class, () -> {
            bidsManagementService.validateBid(bid, auction);
        });
    }

    @Test
    void t4() {
        BidsManagementService bidsManagementService = new BidsManagementService(
            mockAuctionManagementService,
            mockAccountRepository,
            mockAuctionRepository,
            mockBidRepository,
            mockNotificationManagementService
        );
        Account auctionCreator = new ExampleAccount("Roger", "Penske", 1);
        Account bidder = new ExampleAccount("Reinhold", "Joest", 2);        
        Auction auction = new ExampleMountainBikeAuction(
            1, 
            auctionCreator.getId(), 
            null, 
            500, 
            AuctionType.SILENT
        );
        Bid bid = new ExampleMountainBikeBid(auction.getId(), bidder.getId(), null);
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            bidsManagementService.validateBid(bid, auction);
        });
    }

    @Test
    void t5() {
        BidsManagementService bidsManagementService = new BidsManagementService(
            mockAuctionManagementService,
            mockAccountRepository,
            mockAuctionRepository,
            mockBidRepository,
            mockNotificationManagementService
        );
        Account auctionCreator = new ExampleAccount("Roger", "Penske", 1);
        Account bidder = new ExampleAccount("Reinhold", "Joest", 2);        
        Auction auction = new ExampleMountainBikeAuction(
            1, 
            auctionCreator.getId(), 
            null, 
            500, 
            AuctionType.SILENT
        );
        Bid bid = new ExampleMountainBikeBid(auction.getId() + 1, bidder.getId(), 700);
        Assert.assertThrows(InternalError.class, () -> {
            bidsManagementService.validateBid(bid, auction);
        });
    }

    @Test
    void t6() {
        BidsManagementService bidsManagementService = new BidsManagementService(
            mockAuctionManagementService,
            mockAccountRepository,
            mockAuctionRepository,
            mockBidRepository,
            mockNotificationManagementService
        );
        Account auctionCreator = new ExampleAccount("Roger", "Penske", 1);
        Account bidder = new ExampleAccount("Reinhold", "Joest", 2);        
        Auction auction = new ExampleMountainBikeAuction(
            1, 
            auctionCreator.getId(), 
            null, 
            500, 
            AuctionType.SILENT
        );
        Mockito.when(mockAccountRepository.findById(Mockito.any(Integer.class)))
            .thenAnswer(invocation -> {
                Integer id = invocation.getArgument(0);
                return switch (id) {
                    case null -> Optional.empty();
                    case Integer i when Objects.equals(i, bidder.getId()) -> Optional.of(bidder);
                    case Integer i when Objects.equals(i, auctionCreator.getId()) -> Optional.of(auctionCreator);
                    default -> Optional.empty();
                };
            }
        );
        Mockito.when(mockAuctionRepository.findById(auction.getId())).thenReturn(Optional.empty());
        Bid bid = new ExampleMountainBikeBid(auction.getId(), bidder.getId(), 700);
        Assert.assertThrows(NoSuchAuctionException.class, () -> {
            bidsManagementService.validateBid(bid, auction);
        });
    }

    @Test
    void t7() {
        BidsManagementService bidsManagementService = new BidsManagementService(
            mockAuctionManagementService,
            mockAccountRepository,
            mockAuctionRepository,
            mockBidRepository,
            mockNotificationManagementService
        );
        Account auctionCreator = new ExampleAccount("Roger", "Penske", 1);
        Account bidder = new ExampleAccount("Reinhold", "Joest", 2);        
        Auction auction = new ExampleMountainBikeAuction(
            1, 
            auctionCreator.getId(), 
            null, 
            500, 
            AuctionType.SILENT
        );
        Mockito.when(mockAccountRepository.findById(Mockito.any(Integer.class)))
            .thenAnswer(invocation -> {
                Integer id = invocation.getArgument(0);
                return switch (id) {
                    case null -> Optional.empty();
                    case Integer i when Objects.equals(i, bidder.getId()) -> Optional.of(bidder);
                    case Integer i when Objects.equals(i, auctionCreator.getId()) -> Optional.of(auctionCreator);
                    default -> Optional.empty();
                };
            }
        );
        Bid bid = new ExampleMountainBikeBid(auction.getId(), -1, 700);
        Assert.assertThrows(NoAccountWithSuchIdException.class, () -> {
            bidsManagementService.validateBid(bid, auction);
        });
    }

    @Test
    void t8() {
        BidsManagementService bidsManagementService = new BidsManagementService(
            mockAuctionManagementService,
            mockAccountRepository,
            mockAuctionRepository,
            mockBidRepository,
            mockNotificationManagementService
        );
        Account auctionCreator = new ExampleAccount("Roger", "Penske", 1);
        Account bidder = new ExampleAccount("Reinhold", "Joest", 2);        
        Auction auction = new ExampleMountainBikeAuction(
            1, 
            auctionCreator.getId(), 
            2000, 
            null, 
            AuctionType.REVERSE
        );
        BigDecimal bidAmount = auction.getMaximumBid().add(new BigDecimal(100));
        Bid bid = new ExampleMountainBikeBid(auction.getId(), bidder.getId(), bidAmount);
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            bidsManagementService.validateBid(bid, auction);
        });
    }

    @Test
    void t9() {
        BidsManagementService bidsManagementService = new BidsManagementService(
            mockAuctionManagementService,
            mockAccountRepository,
            mockAuctionRepository,
            mockBidRepository,
            mockNotificationManagementService
        );
        Account auctionCreator = new ExampleAccount("Roger", "Penske", 1);
        Account bidder = new ExampleAccount("Reinhold", "Joest", 2);        
        Auction auction = new ExampleMountainBikeAuction(
            1, 
            auctionCreator.getId(), 
            null, 
            500, 
            AuctionType.SILENT
        );
        BigDecimal bidAmount = new BigDecimal(0);
        Bid bid = new ExampleMountainBikeBid(auction.getId(), bidder.getId(), bidAmount);
        Assert.assertThrows(IllegalArgumentException.class, () -> {
            bidsManagementService.validateBid(bid, auction);
        });
    }
}