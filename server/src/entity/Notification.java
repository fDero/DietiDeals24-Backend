package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "Notification")
public class Notification {
    
    @Column(name = "silent_auction_id")
    Integer silentAuctionId;
    
    @Column(name = "reverse_auction_id")
    Integer reverseAuctionId;

    @Column(name = "notification_type")
    String notificationType;

    @Column(name = "account_id")
    Integer accountId;

    @Column(name = "visualized")
    Boolean visualized;

    @Column(name = "email")
    String email;

    @Id @Column(name = "notification_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
