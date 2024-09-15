package entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


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

    @Column(name = "payment_informations")
    private String paymentInformations;

    @Column(name = "bid_date")
    private Timestamp bidDate;

    @Id @Column(name = "bid_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_methods_seq")
    @SequenceGenerator(name = "payment_methods_seq", sequenceName = "payment_methods_ids", allocationSize = 50, initialValue = 1)
    private Integer id;
}
