package response;

import java.sql.Timestamp;
import java.util.List;
import entity.Account;
import entity.ContactInformation;
import entity.PersonalLink;
import utils.AccountPrivateProfileInformationsSerializer;

import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = AccountPrivateProfileInformationsSerializer.class)
@Setter @Getter
public class AccountPrivateProfileInformations {
    
    public AccountPrivateProfileInformations(
        Account account, 
        List<PersonalLink> personalLinks, 
        List<ContactInformation> contactInformation
    ) {
        this.name = account.getName();
        this.surname = account.getSurname();
        this.birthday = account.getBirthday();
        this.country = account.getCountry();
        this.city = account.getCity();
        this.email = account.getEmail();
        this.username = account.getUsername();
        this.accountCreation = account.getAccountCreation();
        this.lastLogin = account.getLastLogin();
        this.profilePictureUrl = account.getProfilePictureUrl();
        this.bio = account.getBio();
        this.unreadNotificationsCounter = account.getUnreadNotificationsCounter();
        this.onlineAuctionsCounter = account.getOnlineAuctionsCounter();
        this.pastDealsCounter = account.getPastDealsCounter();
        this.personalLinks = personalLinks;
        this.contactInformation = contactInformation;
    }

    private String name;
    private String surname;
    private Timestamp birthday;
    private String country;
    private String city; 
    private String email; 
    private String username;
    private Timestamp accountCreation;
    private Timestamp lastLogin;
    private String profilePictureUrl;
    private String bio;
    private Integer unreadNotificationsCounter;
    private Integer onlineAuctionsCounter;
    private Integer pastDealsCounter;
    
    private List<PersonalLink> personalLinks;
    private List<ContactInformation> contactInformation;
}