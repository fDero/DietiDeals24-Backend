package json;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import entity.Account;
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
        final Account account = infos.getAccount();
        final List<PersonalLink> personalLinks = infos.getPersonalLinks();
        gen.writeStartObject();
        AccountSerializerHelper.serializeMinimalBasics(gen, account);
        AccountSerializerHelper.serializeFullBasics(gen, account);
        AccountSerializerHelper.serializeBio(gen, account);
        AccountSerializerHelper.serializeLinks(personalLinks, gen);
        AccountSerializerHelper.serializePublicInformations(gen, infos);
        gen.writeEndObject();
    }
}