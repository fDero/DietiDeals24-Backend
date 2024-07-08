package utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import response.SpecificAuctionPublicInformations;

public class SpecificAuctionPublicInformationsSerializer extends JsonSerializer<SpecificAuctionPublicInformations> {

    @Override
    public void serialize(
        SpecificAuctionPublicInformations value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    { 
        gen.writeStartObject();
        gen.writeStringField("id", value.getId().toString());
        gen.writeStringField("title", value.getItemName());
        gen.writeStringField("country", value.getCountry());
        gen.writeStringField("city", value.getCity());
        gen.writeStringField("conditions", value.getItemCondition());
        gen.writeStringField("type", value.getAuctionType());
        if (value.getAuctionType().equals("silent")) {
            gen.writeNumberField("minimumBid", value.getMinimumBid());
        } 
        else if (value.getAuctionType().equals("reverse")) {
            gen.writeNumberField("maximumBid", value.getMaximumBid());
            gen.writeNumberField("lowestBidSoFar", value.getLowestBidSoFar());
        }
        gen.writeStringField("endTime", value.getEndTime().toString());
        String[] pictureUrls = value.getPicturesUrls();
        String firstPictureUrl = pictureUrls.length > 0 ? pictureUrls[0] : null;
        gen.writeStringField("pictureUrl", firstPictureUrl);
        gen.writeStringField("status", "ACTIVE");
        gen.writeStringField("currency", value.getCurrency());
        gen.writeEndObject();
    }

}
