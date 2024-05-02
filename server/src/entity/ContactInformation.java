package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "ContactInformation")
public class ContactInformation {
    
    @Column(name = "email")
    String email;
    
    @Column(name = "phone")
    String phone;

    @Column(name = "account_id")
    Integer accountId;

    @Id @Column(name = "contact_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
