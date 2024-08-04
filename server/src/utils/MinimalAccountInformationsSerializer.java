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
        gen.writeStringField("email", value.getAccount().getEmail());
        gen.writeStringField("username", value.getAccount().getUsername());
        gen.writeStringField("userId", value.getAccount().getId().toString());
        gen.writeStringField("profilePictureUrl", value.getAccount().getProfilePictureUrl());
        gen.writeEndObject();
    }
    
}
