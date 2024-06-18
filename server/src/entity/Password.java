package entity;

import java.sql.Timestamp;
import java.time.Instant;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Table(name = "Password")
public class Password {
    
    public Password(String passwordSalt, String passwordHash, Integer accountId){
        this.passwordSalt = passwordSalt;
        this.passwordHash = passwordHash;
        this.accountId = accountId;
    }

    @Column(name = "password_last_change")
    private Timestamp passwordLastChange = Timestamp.from(Instant.now());

    @Column(name = "password_salt")
    private String passwordSalt;

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "account_id")
    private Integer accountId;

    @Id @Column(name = "password_id") 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
