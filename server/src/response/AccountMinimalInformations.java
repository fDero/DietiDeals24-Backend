package response;

import com.fasterxml.jackson.core.JsonGenerator;
import utils.AccountAwareJsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Account;
import java.io.IOException;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonSerialize(using = AccountMinimalInformations.class)
@AllArgsConstructor @Getter 
@NoArgsConstructor @Setter
public class AccountMinimalInformations extends AccountAwareJsonSerializer<AccountMinimalInformations> {
    
    private Account account;

    @Override
    public void serialize(
        AccountMinimalInformations minimalAccountInformations,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        gen.writeStartObject();
        serializeMinimalBasics(gen, minimalAccountInformations.getAccount());
        gen.writeEndObject();
    }
}
