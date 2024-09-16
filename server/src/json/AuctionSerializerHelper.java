package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import entity.Auction;

public abstract class AuctionSerializerHelper {

    static public void serializeBasics(JsonGenerator gen, Auction auction)
        throws IOException
    {
        gen.writeStringField("id", auction.getId().toString());
        gen.writeStringField("title", auction.getItemName());
        gen.writeStringField("country", auction.getCountry());
        gen.writeStringField("city", auction.getCity());
        gen.writeStringField("conditions", auction.getItemCondition());
        gen.writeStringField("type", auction.getAuctionType());
        gen.writeStringField("category", auction.getItemCategory());
        gen.writeStringField("macroCategory", auction.getMacroCategory());
        gen.writeStringField("userId", auction.getCreatorId().toString());
        gen.writeStringField("endTime", auction.getEndTime().toString());
        gen.writeStringField("status", auction.getStatus());
        gen.writeStringField("currency", auction.getCurrency());
    }

    static void serializeBidsData(JsonGenerator gen, Auction auction, boolean addOwnerInfos) 
        throws IOException
    {
        gen.writeNumberField("maximumBid", auction.getMaximumBid());
        gen.writeNumberField("minimumBid", auction.getMinimumBid());
        boolean isNotSilent = !auction.getAuctionType().equals("silent");
        boolean isNotActive = !auction.getStatus().equals("active");
        if (isNotSilent || isNotActive || addOwnerInfos) { 
            gen.writeNumberField("highestBidSoFar", auction.getHighestBidSoFar());
            gen.writeNumberField("lowestBidSoFar", auction.getLowestBidSoFar());
            gen.writeNumberField("numberOfBids", auction.getNumberOfBids());
            gen.writeNumberField("currentBidderId", auction.getCurrentBidderId());    
        }
    }

    static void serializeAllPicturesUrls(JsonGenerator gen, Auction auction) 
        throws IOException
    {
        gen.writeArrayFieldStart("picturesUrls");
        for (String pictureUrl : auction.getPicturesUrls()){
            gen.writeString(pictureUrl);
        }
        gen.writeEndArray();    
    }

    static void serializeJustOnePictureUrl(JsonGenerator gen, Auction auction) 
        throws IOException
    {
        String pictureUrls[] = auction.getPicturesUrls();
        String picturesUrl = pictureUrls.length > 0 ? pictureUrls[0] : null;
        gen.writeStringField("picturesUrl", picturesUrl);
    }

    static void serializeDescription(JsonGenerator gen, Auction auction) 
        throws IOException
    {
        gen.writeStringField("description", auction.getDescription());
    }
}
