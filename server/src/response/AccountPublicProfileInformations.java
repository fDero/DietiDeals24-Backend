package response;

import java.util.List;
import entity.Account;
import entity.PersonalLink;
import utils.AccountPublicProfileInformationsSerializer;
import lombok.Getter;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = AccountPublicProfileInformationsSerializer.class)
@Setter @Getter
public class AccountPublicProfileInformations {

    public AccountPublicProfileInformations(Account account, List<PersonalLink> personalLinks) {
        this.username = account.getUsername();
        this.bio = account.getBio();
        this.profilePictureUrl = account.getProfilePictureUrl();
        this.onlineAuctionsCounter = account.getOnlineAuctionsCounter();
        this.pastDealsCounter = account.getPastDealsCounter();
        this.personalLinks = personalLinks;
    }

    private String  username;
    private String  bio;
    private String  profilePictureUrl;
    private Integer onlineAuctionsCounter;
    private Integer pastDealsCounter;
    private List<PersonalLink> personalLinks;
}