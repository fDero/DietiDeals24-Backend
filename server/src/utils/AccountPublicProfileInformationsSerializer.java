package utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.PersonalLink;
import response.AccountPublicProfileInformations;

public class AccountPublicProfileInformationsSerializer extends JsonSerializer<AccountPublicProfileInformations> {
    
    @Override
    public void serialize(
        AccountPublicProfileInformations value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException 
    {
        gen.writeStartObject();
        serializeLinks(value, gen, serializers);
        gen.writeStringField("username", value.getAccount().getUsername());
        gen.writeStringField("bio", value.getAccount().getBio());
        gen.writeStringField("profilePictureUrl", value.getAccount().getProfilePictureUrl());
        gen.writeNumberField("onlineAuctionsCounter", value.getOnlineAuctionsCounter());
        gen.writeNumberField("pastDealsCounter", value.getPastDealsCounter());
        gen.writeStringField("userId", value.getAccount().getId().toString());
        gen.writeEndObject();
    }

    private void serializeLinks(
        AccountPublicProfileInformations value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException 
    {    
        gen.writeArrayFieldStart("links");
        for (PersonalLink link : value.getPersonalLinks()) {
            gen.writeStartObject();
            gen.writeStringField("link", link.getLink());
            gen.writeStringField("description", link.getDescription());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}