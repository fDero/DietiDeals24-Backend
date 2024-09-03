package entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "Activity")
public class Activity {

    @Column(name = "maximum_bid")
    private BigDecimal maximumBid;

    @Column(name = "minimum_bid")
    private BigDecimal minimumBid;

    @Column(name = "number_of_bids")
    private Integer numberOfBids;

    @Column(name = "lowest_bid_so_far")
    private BigDecimal lowestBidSoFar;

    @Column(name = "highest_bid_so_far")
    private BigDecimal highestBidSoFar;

    @Column(name = "current_bidder_id")
    private BigDecimal currentBidderId;

    @Column(name = "creator_id")
    private Integer creatorId;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "item_condition")
    private String itemCondition;

    @Column(name = "item_category")
    private String itemCategory;

    @Column(name = "macro_category")
    private String macroCategory;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "description")
    private String description;

    @Column(name = "pictures_urls")
    private String[] picturesUrls;

    @Column(name = "auction_type")
    private String auctionType;

    @Column(name = "currency")
    private String currency;

    @Column(name = "status")
    private String status;

    @Column(name = "bid_id")
    private Integer bidId;
    
    @Column(name = "bidder_id")
    private Integer bidderId;

    @Column(name = "bid_amount")
    private BigDecimal bidAmount;

    @Column(name = "bid_date")
    private Timestamp bidDate;

    @Id @Column(name = "auction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer auctionId;
}
