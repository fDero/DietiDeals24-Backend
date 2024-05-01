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
    
    @Column(name = "link")
    String email;
    
    @Column(name = "description")
    String phone;

    @Column(name = "account_id")
    Long account_id;

    @Id @Column(name = "contact_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
