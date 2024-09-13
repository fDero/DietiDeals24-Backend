package json;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.PersonalLink;
import entity.Account;
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
        final Account account = infos.getAccount();
        final List<PersonalLink> personalLinks = infos.getPersonalLinks();
        gen.writeStartObject();
        AccountSerializerHelper.serializeMinimalBasics(gen, account);
        AccountSerializerHelper.serializeFullBasics(gen, account);
        AccountSerializerHelper.serializeBio(gen, account);
        AccountSerializerHelper.serializeLinks(personalLinks, gen);
        AccountSerializerHelper.serializePrivateInfomations(gen, infos);
        gen.writeEndObject();
    }
}