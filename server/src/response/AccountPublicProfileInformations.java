package response;

import java.util.List;
import entity.Account;
import entity.PersonalLink;
import utils.AccountPublicProfileInformationsSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = AccountPublicProfileInformationsSerializer.class)
@Setter @NoArgsConstructor 
@Getter @AllArgsConstructor
public class AccountPublicProfileInformations {

    private Account account;
    private List<PersonalLink> personalLinks;
    private long onlineAuctionsCounter;
    private long pastDealsCounter;
}