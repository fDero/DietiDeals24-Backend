package response;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import utils.SpecificAuctionPublicInformationsSerializer;
import entity.Auction;

@JsonSerialize(using = SpecificAuctionPublicInformationsSerializer.class)
@Setter @Getter
public class SpecificAuctionPublicInformations {

    public SpecificAuctionPublicInformations(Auction auction) {
        this.maximumBid = auction.getMaximumBid();
        this.minimumBid = auction.getMinimumBid();
        this.numberOfBids = auction.getNumberOfBids();
        this.lowestBidSoFar = auction.getLowestBidSoFar();
        this.creatorId = auction.getCreatorId();
        this.country = auction.getCountry();
        this.city = auction.getCity();
        this.itemCondition = auction.getItemCondition();
        this.itemCategory = auction.getItemCategory();
        this.macroCategory = auction.getMacroCategory();
        this.startTime = auction.getStartTime();
        this.endTime = auction.getEndTime();
        this.itemName = auction.getItemName();
        this.description = auction.getDescription();
        this.picturesUrls = auction.getPicturesUrls();
        this.auctionType = auction.getAuctionType();
        this.currency = auction.getCurrency();
        this.id = auction.getId();
    }    

    private BigDecimal maximumBid;
    private BigDecimal minimumBid;
    private Integer numberOfBids;
    private BigDecimal lowestBidSoFar;
    private Integer creatorId;
    private String country;
    private String city;
    private String itemCondition;
    private String itemCategory;
    private String macroCategory;
    private Timestamp startTime;
    private Timestamp endTime;
    private String itemName;
    private String description;
    private String[] picturesUrls;
    private String auctionType;
    private String currency;
    private Integer id;
}
