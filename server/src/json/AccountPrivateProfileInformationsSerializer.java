package json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.PersonalLink;
import response.AccountPrivateProfileInformations;

public class AccountPrivateProfileInformationsSerializer extends JsonSerializer<AccountPrivateProfileInformations> {
    
    @Override
    public void serialize(
        AccountPrivateProfileInformations infos, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException 
    {
        String birthayString = infos.getAccount().getBirthday()
            .toLocalDateTime().toLocalDate().toString();

        gen.writeStartObject();
        serializeLinks(infos, gen, serializers);
        serializeCounters(infos, gen, serializers);
        gen.writeStringField("name", infos.getAccount().getName());
        gen.writeStringField("surname", infos.getAccount().getSurname());
        gen.writeStringField("birthday", birthayString);
        gen.writeStringField("country", infos.getAccount().getCountry());
        gen.writeStringField("city", infos.getAccount().getCity());
        gen.writeStringField("email", infos.getAccount().getEmail());
        gen.writeStringField("username", infos.getAccount().getUsername());
        gen.writeStringField("bio", infos.getAccount().getBio());
        gen.writeStringField("profilePictureUrl", infos.getAccount().getProfilePictureUrl());
        gen.writeStringField("userId", infos.getAccount().getId().toString());
        gen.writeEndObject();
    }

    private void serializeLinks(
        AccountPrivateProfileInformations infos, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException 
    {    
        gen.writeArrayFieldStart("links");
        for (PersonalLink link : infos.getPersonalLinks()) {
            gen.writeStartObject();
            gen.writeStringField("link", link.getLink());
            gen.writeStringField("description", link.getDescription());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }

    private void serializeCounters(
        AccountPrivateProfileInformations infos, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException 
    {
        gen.writeNumberField("onlineAuctionsCounter", infos.getOnlineAuctionsCounter());
        gen.writeNumberField("onlineBidsCounter", infos.getOnlineBidsCounter());
        gen.writeNumberField("pastDealsCounter", infos.getPastDealsCounter());
        gen.writeNumberField("pastBidsCounter", infos.getPastBidsCounter());
        gen.writeNumberField("pastAuctionsCounter", infos.getPastAuctionsCounter());
    }
}