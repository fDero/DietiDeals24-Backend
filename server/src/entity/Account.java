package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.PendingAccountRegistration;

import java.sql.Date;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Account {

    public Account(PendingAccountRegistration pending_account, String passwordHash, String passwordSalt) {
        this.name = pending_account.getName();
        this.surname = pending_account.getSurname();
        this.birthday = pending_account.getBirthday();
        this.country = pending_account.getCountry();
        this.city = pending_account.getCity();
        this.email = pending_account.getEmail();
        this.username = pending_account.getUsername();
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.passwordLastChange = new Date(System.currentTimeMillis());
    }

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthday")
    private Date   birthday;

    @Column(name = "country")
    private String country;
    
    @Column(name = "city")
    private String city;

    @Column(name = "email") 
    private String email;

    @Column(name = "username") 
    private String username;

    @Column(name = "password_hash") 
    private String passwordHash;
    
    @Column(name = "password_salt") 
    private String passwordSalt;

    @Column(name = "password_last_change")
    private Date passwordLastChange;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "bio")
    private String bio;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
