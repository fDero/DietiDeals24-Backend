package json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import entity.Auction;
import entity.Bid;
import response.SpecificAuctionInformations;

public class SpecificAuctionInformationsSerializer extends JsonSerializer<SpecificAuctionInformations> {

    @Override
    public void serialize(
        SpecificAuctionInformations specificAuctionPublicInformations, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    { 
        final Auction auction = specificAuctionPublicInformations.getAuction();
        final boolean isOwner = auction.getCreatorId() == specificAuctionPublicInformations.getRequesterId();
        gen.writeStartObject();
        AuctionSerializerHelper.serializeBasics(gen, auction);
        AuctionSerializerHelper.serializeBidsData(gen, auction, isOwner);
        AuctionSerializerHelper.serializeAllPicturesUrls(gen, auction);
        AuctionSerializerHelper.serializeDescription(gen, auction);
        gen.writeArrayFieldStart("ownBids");
        if (specificAuctionPublicInformations.getRequesterBids() != null){            
            for (Bid b : specificAuctionPublicInformations.getRequesterBids()) {
                gen.writeStartObject();
                gen.writeNumberField("bidId", b.getId());
                gen.writeNumberField("auctionId", b.getAuctionId());
                gen.writeNumberField("bidderId", b.getBidderId());
                gen.writeNumberField("bidAmount", b.getBidAmount());
                gen.writeStringField("bidDate", b.getBidDate().toString());
                gen.writeEndObject();
            }
        }
        gen.writeEndArray();
        gen.writeEndObject();
    }
}
