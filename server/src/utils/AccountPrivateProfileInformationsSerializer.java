package utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.ContactInformation;
import entity.PersonalLink;
import response.AccountPrivateProfileInformations;

public class AccountPrivateProfileInformationsSerializer extends JsonSerializer<AccountPrivateProfileInformations> {
    
    @Override
    public void serialize(
        AccountPrivateProfileInformations value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException 
    {
        String birthayString = value.getBirthday()
            .toLocalDateTime().toLocalDate().toString();

        gen.writeStartObject();
        serializeLinks(value, gen, serializers);
        serializeContactInformations(value, gen, serializers);
        gen.writeStringField("name", value.getName());
        gen.writeStringField("surname", value.getSurname());
        gen.writeStringField("birthday", birthayString);
        gen.writeStringField("country", value.getCountry());
        gen.writeStringField("city", value.getCity());
        gen.writeStringField("email", value.getEmail());
        gen.writeStringField("username", value.getUsername());
        gen.writeStringField("bio", value.getBio());
        gen.writeStringField("profilePictureUrl", value.getProfilePictureUrl());
        gen.writeNumberField("onlineAuctionsCounter", value.getOnlineAuctionsCounter());
        gen.writeNumberField("pastDealsCounter", value.getPastDealsCounter());
        gen.writeEndObject();
    }

    private void serializeLinks(
        AccountPrivateProfileInformations value, 
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

    private void serializeContactInformations(
        AccountPrivateProfileInformations value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException 
    {    
        gen.writeArrayFieldStart("contactInformations");
        for (ContactInformation info : value.getContactInformation()) {
            gen.writeStartObject();
            gen.writeStringField("email", info.getEmail());
            gen.writeStringField("phone", info.getPhone());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}