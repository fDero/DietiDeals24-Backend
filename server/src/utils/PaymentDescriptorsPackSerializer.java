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
            gen.writeStringField("cardNumber", card.getCardNumber());
            gen.writeStringField("expirationDate", card.getExpirationDate().toString());
            gen.writeStringField("name", card.getName());
            gen.writeStringField("address", card.getAddress());
            gen.writeStringField("country", card.getCountry());
            gen.writeStringField("zip", card.getZip());
            gen.writeNumberField("id", card.getId());
            gen.writeEndObject();
        }
        for (Iban iban : value.getIbans()) {
            gen.writeStartObject();
            gen.writeStringField("iban", iban.getIbanString());
            gen.writeNumberField("id", iban.getId());
            gen.writeEndObject();
        }
        gen.writeEndObject();
    }
    
}
