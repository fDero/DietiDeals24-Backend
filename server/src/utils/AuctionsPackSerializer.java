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
        gen.writeStartArray();
        for (Auction auction : value.getAuctions()) {
            gen.writeStartObject();
            gen.writeStringField("id", auction.getId().toString());
            gen.writeStringField("title", auction.getItemName());
            gen.writeStringField("country", auction.getCountry());
            gen.writeStringField("city", auction.getCity());
            gen.writeStringField("conditions", auction.getItemCondition());
            gen.writeStringField("type", auction.getAuctionType());
            if (auction.getAuctionType().equals("REVERSE")) {
                gen.writeObjectField("minimumBid", 
                    new Object() {
                        Number amount = auction.getPrice();
                        String currency = "EUR";
                    }
                );
            } else if (auction.getAuctionType().equals("SILENT")) {
                gen.writeObjectField("minimumBid", 
                    new Object() {
                        Number amount = auction.getPrice();
                        String currency = "EUR";
                    }
                );
                gen.writeObjectField("maximumBid", 
                    new Object() {
                        Number amount = auction.getPrice();
                        String currency = "EUR";
                    }
                );
            }
            gen.writeStringField("endTime", auction.getEndTime().toString());
            String[] pictureUrls = auction.getPicturesUrls();
            String firstPictureUrl = pictureUrls.length > 0 ? pictureUrls[0] : null;
            gen.writeStringField("pictureUrl", firstPictureUrl);
            gen.writeStringField("status", "ACTIVE");
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}