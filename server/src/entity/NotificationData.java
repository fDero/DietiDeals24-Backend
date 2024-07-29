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
@Table(name = "NotificationData")
public class NotificationData {
    
    @Column(name = "auction_id")
    Integer auctionId;

    @Column(name = "notification_type")
    String notificationType;

    @Column(name = "account_username")
    String accountUsername;

    @Column(name = "visualized")
    Boolean visualized;

    @Column(name = "eliminated")
    Boolean eliminated;

    @Id @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}