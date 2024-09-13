package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import response.AccountMinimalInformations;
import entity.Account;

public class AccountMinimalInformationsSerializer extends JsonSerializer<AccountMinimalInformations> {

    @Override
    public void serialize(
        AccountMinimalInformations minimalAccountInformations, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    {
        final Account account = minimalAccountInformations.getAccount();
        gen.writeStartObject();
        AccountSerializerHelper.serializeMinimalBasics(gen, account);
        gen.writeEndObject();
    }
    
}
