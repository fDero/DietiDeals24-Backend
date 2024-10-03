package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "OAuthAccountBinding")
public class OAuthAccountBinding {
    
    @Column(name = "internal_account_id")
    Integer internalAccountId;

    @Column(name = "oauth_account_id")
    String oauthAccountId;

    @Column(name = "oauth_provider")
    String oauthProvider;

    @Id @Column(name = "oauth_binding_pk")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pk;
}