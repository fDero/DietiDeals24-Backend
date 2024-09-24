package response;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.CreditCard;
import entity.Iban;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = PaymentDescriptorsPack.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class PaymentDescriptorsPack extends JsonSerializer<PaymentDescriptorsPack> {

    private List<CreditCard> creditCards;
    private List<Iban> ibans;

    @Override
    public void serialize(
        PaymentDescriptorsPack paymentDescriptorsPack,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        gen.writeStartArray();
        for (CreditCard card : paymentDescriptorsPack.getCreditCards()) {
            gen.writeStartObject();
            gen.writeStringField("type", "CREDIT_CARD");
            gen.writeStringField("last4digits", card.getLast4digits());
            gen.writeNumberField("id", card.getId());
            gen.writeEndObject();
        }
        for (Iban iban : paymentDescriptorsPack.getIbans()) {
            gen.writeStartObject();
            gen.writeStringField("type", "IBAN");
            gen.writeStringField("iban", iban.getIbanString());
            gen.writeNumberField("id", iban.getId());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}
