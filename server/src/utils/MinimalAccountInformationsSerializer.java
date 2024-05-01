package utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import response.MinimalAccountInformations;

public class MinimalAccountInformationsSerializer extends JsonSerializer<MinimalAccountInformations> {

    @Override
    public void serialize(
        MinimalAccountInformations value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    {
        gen.writeStartObject();
        gen.writeStringField("email", value.getEmail());
        gen.writeStringField("username", value.getUsername());
        gen.writeStringField("profilePictureUrl", value.getProfilePictureUrl());
        gen.writeNumberField("unreadNotificationsCounter", value.getUnreadNotificationsCounter());
        gen.writeEndObject();
    }
    
}
