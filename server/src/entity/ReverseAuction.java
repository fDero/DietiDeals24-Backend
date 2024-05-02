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

    @Column(name = "max_price", nullable = false)
    private BigDecimal maxPrice;

    @Column(name = "creator_id", nullable = false)
    private Integer creatorId;

    @Column(name = "country", nullable = false, length = 5)
    private String country;

    @Column(name = "city", nullable = false, length = 50)
    private String city;

    @Column(name = "item_condition", columnDefinition = "TEXT")
    private String itemCondition;

    @Column(name = "item_category", columnDefinition = "TEXT")
    private String itemCategory;

    @Column(name = "macro_category", columnDefinition = "TEXT")
    private String macroCategory;

    @Column(name = "start_time", nullable = false)
    private Timestamp startDate;

    @Column(name = "end_time", nullable = false)
    private Timestamp endDate;

    @Column(name = "item_name", nullable = false, columnDefinition = "TEXT")
    private String itemName;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "pictures_urls", columnDefinition = "TEXT[]")
    private String[] picturesUrls;

    @Column(name = "creation_time")
    private Timestamp creationTime;

    @Column(name = "reverse_auction_id", nullable = false)
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}