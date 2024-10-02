package utils;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import entity.Auction;

public abstract class AuctionAwareJsonSerializer<T> extends JsonSerializer<T> {

    public static void serializeBasics(JsonGenerator gen, Auction auction)
        throws
            IOException
    {
        String endTimeString = TimestampFormatter.convertTimestampToISOFormatUTC(
            auction.getEndTime()
        );
        gen.writeStringField("id", auction.getId().toString());
        gen.writeStringField("title", auction.getItemName());
        gen.writeStringField("country", auction.getCountry());
        gen.writeStringField("city", auction.getCity());
        gen.writeStringField("conditions", auction.getItemCondition());
        gen.writeStringField("type", auction.getAuctionType());
        gen.writeStringField("category", auction.getItemCategory());
        gen.writeStringField("macroCategory", auction.getMacroCategory());
        gen.writeStringField("userId", auction.getCreatorId().toString());
        gen.writeStringField("endTime", endTimeString);
        gen.writeStringField("status", auction.getStatus());
        gen.writeStringField("currency", auction.getCurrency());
    }

    protected static void serializeBidsData(JsonGenerator gen, Auction auction, boolean addOwnerInfos)
        throws
            IOException
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

    protected static void serializeAllPicturesUrls(JsonGenerator gen, Auction auction)
        throws
            IOException
    {
        gen.writeArrayFieldStart("picturesUrls");
        for (String pictureUrl : auction.getPicturesUrls()){
            gen.writeString(pictureUrl);
        }
        gen.writeEndArray();
    }

    protected static void serializeJustOnePictureUrl(JsonGenerator gen, Auction auction)
        throws
            IOException
    {
        String[] pictureUrls = auction.getPicturesUrls();
        String picturesUrl = pictureUrls.length > 0 ? pictureUrls[0] : null;
        gen.writeStringField("picturesUrl", picturesUrl);
    }

    protected static void serializeDescription(JsonGenerator gen, Auction auction)
        throws
            IOException
    {
        gen.writeStringField("description", auction.getDescription());
    }
}