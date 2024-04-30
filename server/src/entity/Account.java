package entity;

import utils.PendingAccountRegistration;
import java.sql.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
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
        this.passwordLastChange = new Date(System.currentTimeMillis());
        this.unreadNotifications = 0L;
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

    @Column(name = "unread_notifications")
    private Long unreadNotifications;

    @Id @Column(name = "account_id") 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
