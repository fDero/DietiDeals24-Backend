package utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import entity.Auction;

public abstract class AuctionSerializerHelper {

    static public void serializeBasics(JsonGenerator gen, Auction value)
        throws IOException
    {
        gen.writeStringField("id", value.getId().toString());
        gen.writeStringField("title", value.getItemName());
        gen.writeStringField("country", value.getCountry());
        gen.writeStringField("city", value.getCity());
        gen.writeStringField("conditions", value.getItemCondition());
        gen.writeStringField("type", value.getAuctionType());
        gen.writeStringField("category", value.getItemCategory());
        gen.writeStringField("macroCategory", value.getMacroCategory());
        gen.writeStringField("userId", value.getCreatorId().toString());
        gen.writeStringField("endTime", value.getEndTime().toString());
        gen.writeStringField("status", value.getStatus());
        gen.writeStringField("currency", value.getCurrency());
    }

    static void serializeBidsData(JsonGenerator gen, Auction value) 
        throws IOException
    {
        gen.writeNumberField("maximumBid", value.getMaximumBid());
        gen.writeNumberField("minimumBid", value.getMinimumBid());
        boolean isNotSilent = !value.getAuctionType().equals("silent");
        boolean isNotActive = !value.getStatus().equals("active");
        if (isNotSilent || isNotActive) { 
            gen.writeNumberField("highestBidSoFar", value.getHighestBidSoFar());
            gen.writeNumberField("lowestBidSoFar", value.getLowestBidSoFar());
            gen.writeNumberField("numberOfBids", value.getNumberOfBids());
            gen.writeNumberField("currentBidderId", value.getCurrentBidderId());    
        }
    }

    static void serializeAllPicturesUrls(JsonGenerator gen, Auction value) 
        throws IOException
    {
        gen.writeArrayFieldStart("picturesUrls");
        for (String pictureUrl : value.getPicturesUrls()){
            gen.writeString(pictureUrl);
        }
        gen.writeEndArray();    
    }

    static void serializeJustOnePictureUrl(JsonGenerator gen, Auction value) 
        throws IOException
    {
        String pictureUrls[] = value.getPicturesUrls();
        String picturesUrl = pictureUrls.length > 0 ? pictureUrls[0] : null;
        gen.writeStringField("picturesUrl", picturesUrl);
    }

    static void serializeDescription(JsonGenerator gen, Auction value) 
        throws IOException
    {
        gen.writeStringField("description", value.getDescription());
    }
}
