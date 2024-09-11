package entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.GenerationType;


@Entity @Table(name = "Bid") 
@Getter @NoArgsConstructor 
@Setter @AllArgsConstructor
public class Bid {
    
    @Column(name = "auction_id")
    private Integer auctionId;
    
    @Column(name = "bidder_id")
    private Integer bidderId;

    @Column(name = "bid_amount")
    private BigDecimal bidAmount;

    @Column(name = "bid_refound_token")
    private String bidRefoundToken;

    @Column(name = "bid_date")
    private Timestamp bidDate;

    @Id @Column(name = "bid_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
