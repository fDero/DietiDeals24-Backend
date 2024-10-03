package entity;

import utils.PendingAccountRegistration;
import utils.TimestampFormatter;
import java.sql.Timestamp;
import request.OAuthAccountRegistrationRequest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "Account")
public class Account {

    public Account(PendingAccountRegistration pendingAccount) {
        this.name = pendingAccount.getName();
        this.surname = pendingAccount.getSurname();
        this.birthday = pendingAccount.getBirthday();
        this.country = pendingAccount.getCountry();
        this.city = pendingAccount.getCity();
        this.email = pendingAccount.getEmail();
        this.username = pendingAccount.getUsername();
        this.accountCreation = new Timestamp(System.currentTimeMillis());
        this.lastLogin = new Timestamp(System.currentTimeMillis());
        this.accountProvider = "DIETIDEALS24";
    }

    public Account(
        String accountProvider, 
        String email, 
        OAuthAccountRegistrationRequest accountRegistrationRequest
    ) {
        this.name = accountRegistrationRequest.getName();
        this.surname = accountRegistrationRequest.getSurname();
        this.birthday = TimestampFormatter.parseFromClientRequest(accountRegistrationRequest.getBirthday());
        this.country = accountRegistrationRequest.getCountry();
        this.city = accountRegistrationRequest.getCity();
        this.email = email;
        this.username = accountRegistrationRequest.getUsername();
        this.accountCreation = new Timestamp(System.currentTimeMillis());
        this.lastLogin = new Timestamp(System.currentTimeMillis());
        this.accountProvider = accountProvider;
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

    @Column(name = "account_creation")
    private Timestamp accountCreation;

    @Column(name = "last_login")
    private Timestamp lastLogin;

    @Column(name = "profile_picture_url")
    private String profilePictureUrl;

    @Column(name = "bio")
    private String bio;
    
    @Column(name = "account_provider")
    private String accountProvider;

    @Id @Column(name = "account_id") 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
}
