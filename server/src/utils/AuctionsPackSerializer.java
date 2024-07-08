package utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Auction;
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
            if (auction.getAuctionType().equals("silent")) {
                gen.writeNumberField("minimumBid", auction.getMinimumBid());
            } else if (auction.getAuctionType().equals("reverse")) {
                gen.writeNumberField("maximumBid", auction.getMaximumBid());
                gen.writeNumberField("lowestBidSoFar", auction.getLowestBidSoFar());
            }
            gen.writeStringField("endTime", auction.getEndTime().toString());
            String[] pictureUrls = auction.getPicturesUrls();
            String firstPictureUrl = pictureUrls.length > 0 ? pictureUrls[0] : null;
            gen.writeStringField("pictureUrl", firstPictureUrl);
            gen.writeStringField("status", "ACTIVE");
            gen.writeStringField("currency", auction.getCurrency());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}