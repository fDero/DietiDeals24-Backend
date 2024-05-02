package utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.ContactInformation;
import entity.PersonalLink;
import response.AccountProfileInformations;

public class AccountProfileInformationsSerializer extends JsonSerializer<AccountProfileInformations> {
    
    @Override
    public void serialize(
            AccountProfileInformations value, 
            JsonGenerator gen, 
            SerializerProvider serializers
    ) 
        throws IOException 
    {
        String birthayString = value.getAccount().getBirthday()
            .toLocalDateTime().toLocalDate().toString();

        gen.writeStartObject();
        serializeLinks(value, gen, serializers);
        serializeContactInformations(value, gen, serializers);
        gen.writeStringField("name", value.getAccount().getName());
        gen.writeStringField("surname", value.getAccount().getSurname());
        gen.writeStringField("birthday", birthayString);
        gen.writeStringField("country", value.getAccount().getCountry());
        gen.writeStringField("city", value.getAccount().getCity());
        gen.writeStringField("email", value.getAccount().getEmail());
        gen.writeStringField("username", value.getAccount().getUsername());
        gen.writeStringField("bio", value.getAccount().getBio());
        gen.writeStringField("profilePictureUrl", value.getAccount().getProfilePictureUrl());
        gen.writeNumberField("onlineAuctionsCounter", value.getAccount().getOnlineAuctionsCounter());
        gen.writeNumberField("pastDealsCounter", value.getAccount().getPastDealsCounter());
        
        gen.writeEndObject();
    }

    private void serializeLinks(
            AccountProfileInformations value, 
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
            AccountProfileInformations value, 
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