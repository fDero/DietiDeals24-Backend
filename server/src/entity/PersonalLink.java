package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "PersonalLink")
public class PersonalLink {
    
    @Column(name = "link")
    String link;
    
    @Column(name = "description")
    String description;

    @Column(name = "account_id")
    Long account_id;

    @Id @Column(name = "link_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
