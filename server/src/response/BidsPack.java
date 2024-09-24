package response;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Bid;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@JsonSerialize(using = BidsPack.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
@ToString
public class BidsPack extends JsonSerializer<BidsPack> {

    private List<Bid> bids;

    @Override
    public void serialize(
        BidsPack bidsPack,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        System.out.println(bidsPack);
        System.out.println(bidsPack.getBids().size());
        gen.writeStartArray();
        for (Bid b : bidsPack.getBids()) {
            gen.writeStartObject();
            gen.writeNumberField("bidId", b.getId());
            gen.writeNumberField("auctionId", b.getAuctionId());
            gen.writeNumberField("bidderId", b.getBidderId());
            gen.writeNumberField("bidAmount", b.getBidAmount());
            gen.writeStringField("bidDate", b.getBidDate().toString());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}