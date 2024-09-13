package json;

import java.io.IOException;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerator;
import entity.Account;
import entity.PersonalLink;
import response.AccountPrivateProfileInformations;
import response.AccountPublicProfileInformations;

public abstract class AccountSerializerHelper {

    static public void serializeMinimalBasics(JsonGenerator gen, Account account)
        throws IOException
    {
        gen.writeStringField("username", account.getUsername());
        gen.writeStringField("userId", account.getId().toString());
        gen.writeStringField("profilePictureUrl", account.getProfilePictureUrl());
        gen.writeStringField("country", account.getCountry());
        gen.writeStringField("city", account.getCity());
    }

    static public void serializeFullBasics(JsonGenerator gen, Account account)
        throws IOException
    {
        final String birthayString = 
            account.getBirthday().toLocalDateTime().toLocalDate().toString();
        gen.writeStringField("name", account.getName());
        gen.writeStringField("surname", account.getSurname());
        gen.writeStringField("birthday", birthayString);
    }

    static public void serializePrivateInfomations(
        JsonGenerator gen, 
        AccountPrivateProfileInformations infos
    )
        throws IOException
    {
        final Account account = infos.getAccount();
        gen.writeStringField("email", account.getEmail());
        gen.writeNumberField("onlineAuctionsCounter", infos.getOnlineAuctionsCounter());
        gen.writeNumberField("onlineBidsCounter", infos.getOnlineBidsCounter());
        gen.writeNumberField("pastDealsCounter", infos.getPastDealsCounter());
        gen.writeNumberField("pastBidsCounter", infos.getPastBidsCounter());
        gen.writeNumberField("pastAuctionsCounter", infos.getPastAuctionsCounter());
    }

    static public void serializePublicInformations(
        JsonGenerator gen, 
        AccountPublicProfileInformations infos
    )
        throws IOException
    {
        gen.writeNumberField("onlineAuctionsCounter", infos.getOnlineAuctionsCounter());
        gen.writeNumberField("pastDealsCounter", infos.getPastDealsCounter());
    }

    static public void serializeBio(JsonGenerator gen, Account account)
        throws IOException
    {
        gen.writeStringField("bio", account.getBio());
    }

    public static void serializeLinks(
        List<PersonalLink> personalLinks, 
        JsonGenerator gen
    ) 
        throws IOException 
    {    
        gen.writeArrayFieldStart("links");
        for (PersonalLink link : personalLinks) {
            gen.writeStartObject();
            gen.writeStringField("link", link.getLink());
            gen.writeStringField("description", link.getDescription());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}
