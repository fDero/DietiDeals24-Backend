package response;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import utils.AuctionAwareJsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Auction;
import entity.Bid;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonSerialize(using = SpecificAuctionInformations.class)
@NoArgsConstructor @Setter 
@AllArgsConstructor @Getter
public class SpecificAuctionInformations extends AuctionAwareJsonSerializer<SpecificAuctionInformations> {

    private Auction auction;
    private Integer requesterId;
    private List<Bid> requesterBids;

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
        serializeBasics(gen, auction);
        serializeBidsData(gen, auction, isOwner);
        serializeAllPicturesUrls(gen, auction);
        serializeDescription(gen, auction);
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
