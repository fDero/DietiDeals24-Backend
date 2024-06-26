package entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "ReverseAuction")
public class ReverseAuction {

    @Column(name = "maximum_bid")
    private BigDecimal maximumBid;

    @Column(name = "lowest_bid_so_far")
    private BigDecimal lowestBidSoFar;

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

    @Column(name = "currency")
    private String currency;

    @Column(name = "auction_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}