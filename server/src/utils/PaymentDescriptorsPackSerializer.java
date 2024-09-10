package utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import entity.CreditCard;
import entity.Iban;
import response.PaymentDescriptorsPack;

public class PaymentDescriptorsPackSerializer extends JsonSerializer<PaymentDescriptorsPack> {

    @Override
    public void serialize(
        PaymentDescriptorsPack value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    {
        gen.writeStartArray();
        for (CreditCard card : value.getCreditCards()) {
            gen.writeStartObject();
            gen.writeStringField("type", "CREDIT_CARD");
            gen.writeStringField("last4digits", card.getLast4digits());
            gen.writeNumberField("id", card.getId());
            gen.writeEndObject();
        }
        for (Iban iban : value.getIbans()) {
            gen.writeStartObject();
            gen.writeStringField("type", "IBAN");
            gen.writeStringField("iban", iban.getIbanString());
            gen.writeNumberField("id", iban.getId());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
    
}
