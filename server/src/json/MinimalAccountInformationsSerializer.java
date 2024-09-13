package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import response.MinimalAccountInformations;
import entity.Account;

public class MinimalAccountInformationsSerializer extends JsonSerializer<MinimalAccountInformations> {

    @Override
    public void serialize(
        MinimalAccountInformations minimalAccountInformations, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    {
        final Account account = minimalAccountInformations.getAccount();
        gen.writeStartObject();
        gen.writeStringField("username", account.getUsername());
        gen.writeStringField("userId", account.getId().toString());
        gen.writeStringField("profilePictureUrl", account.getProfilePictureUrl());
        gen.writeEndObject();
    }
    
}
