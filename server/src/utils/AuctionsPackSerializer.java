package utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Auction;
import entity.Notification;
import response.AuctionsPack;


public class AuctionsPackSerializer extends JsonSerializer<AuctionsPack> {
    
    @Override
    public void serialize(
            AuctionsPack value, 
            JsonGenerator gen, 
            SerializerProvider serializers
    ) 
        throws IOException  
    {
        for (Auction auction : value.getAuctions()) {
            gen.writeStartObject();
            gen.writeStringField("title", auction.getItemName());
            gen.writeStringField("country", auction.getCountry());
            gen.writeStringField("city", auction.getCity());
            gen.writeStringField("conditions", auction.getItemCondition());
            gen.writeStringField("type", auction.getMacroCategory());
            if (auction.getMacroCategory().equals("REVERSE")) {
                gen.writeNumberField("minimumBid", auction.getPrice());
            } else {
                gen.writeNumberField("maximumBid", auction.getPrice());
            }
            gen.writeStringField("endTime", auction.getEndTime().toString());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}