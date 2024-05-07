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
@Table(name = "SilentAuction")
public class SilentAuction {
    
    @Column(name = "minimum_bid")
    private BigDecimal minimumBid;

    @Column(name = "creator_id")
    private Integer creatorId;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "start_time")
    private Timestamp startTime;

    @Column(name = "end_time")
    private Timestamp endTime;

    @Column(name = "item_name")
    private String itemName;

    @Column(name = "description")
    private String description;

    @Column(name = "item_condition")
    private String itemCondition;

    @Column(name = "item_category")
    private String itemCategory;

    @Column(name = "max_price")
    private BigDecimal maxPrice;

    @Column(name = "macro_category")
    private String macroCategory;

    @Column(name = "pictures_urls")
    private String[] picturesUrls;

    @Column(name = "currency")
    private String currency;

    @Id @Column(name = "auction_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
