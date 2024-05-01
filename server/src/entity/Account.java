package entity;

import utils.PendingAccountRegistration;
import java.sql.Timestamp;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "Account")
public class Account {

    public Account(PendingAccountRegistration pendingAccount, String passwordHash, String passwordSalt) {
        this.name = pendingAccount.getName();
        this.surname = pendingAccount.getSurname();
        this.birthday = pendingAccount.getBirthday();
        this.country = pendingAccount.getCountry();
        this.city = pendingAccount.getCity();
        this.email = pendingAccount.getEmail();
        this.username = pendingAccount.getUsername();
        this.passwordHash = passwordHash;
        this.passwordSalt = passwordSalt;
        this.unreadNotificationsCounter = 0L;
        this.onlineAuctionsCounter = 0L;
        this.pastDealsCounter = 0L;
        this.accountCreation = new Timestamp(System.currentTimeMillis());
        this.lastLogin = new Timestamp(System.currentTimeMillis());
        this.passwordLastChange = new Timestamp(System.currentTimeMillis());
    }

    @Column(name = "name")
    private String name;

    @Column(name = "surname")
    private String surname;

    @Column(name = "birthday")
    private Timestamp birthday;

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
    private Timestamp passwordLastChange;

    @Column(name = "account_creation")
    private Timestamp accountCreation;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "bio")
    private String bio;

    @Column(name = "unread_notifications_counter")
    private Long unreadNotificationsCounter;

    @Column(name = "online_auctions_counter")
    private Long onlineAuctionsCounter;

    @Column(name = "past_deals_counter")
    private Long pastDealsCounter;


    @Id @Column(name = "account_id") 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
