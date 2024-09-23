package entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import jakarta.persistence.GenerationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity @Table(name = "Resource") 
@Getter @NoArgsConstructor 
@Setter @AllArgsConstructor
public class Resource {
    
    @Column(name = "resource_url")
    private String resourceUrl;

    @Column(name = "resource_key")
    private String resourceKey;

    @Column(name = "upload_timestamp")
    private Timestamp uploadTimestamp;

    @Column(name = "confirmation_timestamp")
    private Timestamp confirmationTimestamp;
    
    @Id @Column(name = "resource_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
