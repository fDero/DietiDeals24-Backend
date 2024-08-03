package response;

import java.util.List;
import entity.Account;
import entity.PersonalLink;
import utils.AccountPrivateProfileInformationsSerializer;

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
    private long pastDealsCounter;
}