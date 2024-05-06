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
@Table(name = "Auction")
public class Auction {

    @Column(name = "price")
    private BigDecimal price;

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

    @Column(name = "creation_time")
    private Timestamp creationTime;

    @Column(name = "aution_id")
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}