package response;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import utils.AuctionAwareJsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Auction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = AuctionsPack.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class AuctionsPack extends AuctionAwareJsonSerializer<AuctionsPack> {

    private List<Auction> auctions;

    @Override
    public void serialize(
        AuctionsPack auctionsPack,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        gen.writeStartArray();
        for (Auction auction : auctionsPack.getAuctions()) {
            gen.writeStartObject();
            serializeBasics(gen, auction);
            serializeBidsData(gen, auction, false);
            serializeJustOnePictureUrl(gen, auction);
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}