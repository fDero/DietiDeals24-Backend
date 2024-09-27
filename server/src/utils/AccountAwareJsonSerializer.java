package utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import entity.Account;
import entity.PersonalLink;
import response.AccountPrivateProfileInformations;
import response.AccountPublicProfileInformations;

public abstract class AccountAwareJsonSerializer<T> extends JsonSerializer<T> {

    public static void serializeMinimalBasics(JsonGenerator gen, Account account)
            throws IOException
    {
        gen.writeStringField("username", account.getUsername());
        gen.writeStringField("userId", account.getId().toString());
        gen.writeStringField("profilePictureUrl", account.getProfilePictureUrl());
        gen.writeStringField("country", account.getCountry());
        gen.writeStringField("city", account.getCity());
    }

    public static void serializeFullBasics(JsonGenerator gen, Account account)
            throws IOException
    {
        final LocalDateTime birthayLocalDateTime = account.getBirthday().toLocalDateTime();
        final String birthayString = birthayLocalDateTime.toLocalDate().toString();
        gen.writeStringField("name", account.getName());
        gen.writeStringField("surname", account.getSurname());
        gen.writeStringField("birthday", birthayString);
    }

    public static void serializePrivateInfomations(
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

    public static void serializePublicInformations(
            JsonGenerator gen,
            AccountPublicProfileInformations infos
    )
            throws IOException
    {
        gen.writeNumberField("onlineAuctionsCounter", infos.getOnlineAuctionsCounter());
        gen.writeNumberField("pastDealsCounter", infos.getPastDealsCounter());
    }

    public static void serializeBio(JsonGenerator gen, Account account)
            throws IOException
    {
        gen.writeStringField("bio", account.getBio());
    }

    public static void serializeLinks(
            JsonGenerator gen,
            List<PersonalLink> personalLinks
    )
            throws IOException
    {
        gen.writeArrayFieldStart("links");
        for (PersonalLink link : personalLinks) {
            gen.writeStartObject();
            gen.writeStringField("link", link.getLink());
            gen.writeStringField("description", link.getDescription());
            gen.writeNumberField("id", link.getId());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}