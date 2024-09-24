package response;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Account;
import entity.PersonalLink;
import utils.AccountProfileInformations;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.AccountAwareJsonSerializer;


@JsonSerialize(using = AccountPrivateProfileInformations.class)
@NoArgsConstructor @Getter
@AllArgsConstructor @Setter
public class AccountPrivateProfileInformations extends AccountAwareJsonSerializer<AccountPrivateProfileInformations> {
    
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

    @Override
    public void serialize(
        AccountPrivateProfileInformations infos,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        final Account account = infos.getAccount();
        final List<PersonalLink> personalLinks = infos.getPersonalLinks();
        gen.writeStartObject();
        serializeMinimalBasics(gen, account);
        serializeFullBasics(gen, account);
        serializeBio(gen, account);
        serializeLinks(personalLinks, gen);
        serializePrivateInfomations(gen, infos);
        gen.writeEndObject();
    }
}