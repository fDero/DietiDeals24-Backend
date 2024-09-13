package json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import entity.Bid;
import response.BidsPack;

public class BidsPackSerializer extends JsonSerializer<BidsPack> {
    
    @Override
    public void serialize(
        BidsPack bidsPack, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException  
    {
        System.out.println("BidsPackSerializer: serialize");
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