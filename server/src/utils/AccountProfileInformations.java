package utils;

import java.util.List;
import entity.Account;
import entity.PersonalLink;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @Getter
@AllArgsConstructor @Setter
public class AccountProfileInformations {
    
    private Account account;
    private List<PersonalLink> personalLinks;
    private long onlineAuctionsCounter;
    private long onlineBidsCounter;
    private long pastDealsCounter;
    private long pastBidsCounter;
    private long pastAuctionsCounter;
}