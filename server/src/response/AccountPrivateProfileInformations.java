package response;

import java.util.List;
import entity.Account;
import entity.PersonalLink;
import utils.AccountPrivateProfileInformationsSerializer;
import utils.AccountProfileInformations;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = AccountPrivateProfileInformationsSerializer.class)
@NoArgsConstructor @Getter
@AllArgsConstructor @Setter
public class AccountPrivateProfileInformations {
    
    private Account account;
    private List<PersonalLink> personalLinks;
    private long onlineAuctionsCounter;
    private long onlineBidsCounter;
    private long pastDealsCounter;
    private long pastBidsCounter;
    private long pastAuctionsCounter;

    public AccountPrivateProfileInformations(AccountProfileInformations accountProfileInformations) {
        account = accountProfileInformations.getAccount();
        personalLinks = accountProfileInformations.getPersonalLinks();
        onlineAuctionsCounter = accountProfileInformations.getOnlineAuctionsCounter();
        onlineBidsCounter = accountProfileInformations.getOnlineBidsCounter();
        pastDealsCounter = accountProfileInformations.getPastDealsCounter();
        pastBidsCounter = accountProfileInformations.getPastBidsCounter();
        pastAuctionsCounter = accountProfileInformations.getPastAuctionsCounter();
    }
}