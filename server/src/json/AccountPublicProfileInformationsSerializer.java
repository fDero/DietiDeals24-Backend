package json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.PersonalLink;
import response.AccountPublicProfileInformations;

public class AccountPublicProfileInformationsSerializer extends JsonSerializer<AccountPublicProfileInformations> {
    
    @Override
    public void serialize(
        AccountPublicProfileInformations infos, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException 
    {
        gen.writeStartObject();
        serializeLinks(infos, gen, serializers);
        gen.writeStringField("username", infos.getAccount().getUsername());
        gen.writeStringField("bio", infos.getAccount().getBio());
        gen.writeStringField("profilePictureUrl", infos.getAccount().getProfilePictureUrl());
        gen.writeNumberField("onlineAuctionsCounter", infos.getOnlineAuctionsCounter());
        gen.writeNumberField("pastDealsCounter", infos.getPastDealsCounter());
        gen.writeStringField("userId", infos.getAccount().getId().toString());
        gen.writeEndObject();
    }

    private void serializeLinks(
        AccountPublicProfileInformations infos, 
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
}