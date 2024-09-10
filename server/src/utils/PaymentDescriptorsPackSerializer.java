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
        gen.writeStartObject();
        gen.writeArrayFieldStart("creditCards");
        for (CreditCard card : value.getCreditCards()) {
            gen.writeStartObject();
            gen.writeStringField("last4digits", card.getLast4digits());
            gen.writeNumberField("id", card.getId());
            gen.writeEndObject();
        }
        gen.writeEndArray();
        gen.writeArrayFieldStart("ibans");
        for (Iban iban : value.getIbans()) {
            gen.writeStartObject();
            gen.writeStringField("iban", iban.getIbanString());
            gen.writeNumberField("id", iban.getId());
            gen.writeEndObject();
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
    
}
