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


@JsonSerialize(using = AccountPublicProfileInformations.class)
@Setter @NoArgsConstructor 
@Getter @AllArgsConstructor
public class AccountPublicProfileInformations extends AccountAwareJsonSerializer<AccountPublicProfileInformations> {

    private Account account;
    private List<PersonalLink> personalLinks;
    private long onlineAuctionsCounter;
    private long pastDealsCounter;

    public AccountPublicProfileInformations(AccountProfileInformations accountPublicProfileInformations) {
        account = accountPublicProfileInformations.getAccount();
        personalLinks = accountPublicProfileInformations.getPersonalLinks();
        onlineAuctionsCounter = accountPublicProfileInformations.getOnlineAuctionsCounter();
        pastDealsCounter = accountPublicProfileInformations.getPastDealsCounter();
    }

    @Override
    public void serialize(
        AccountPublicProfileInformations infos,
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
        serializePublicInformations(gen, infos);
        gen.writeEndObject();
    }
}