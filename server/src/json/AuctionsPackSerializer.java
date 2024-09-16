package json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Auction;
import response.AuctionsPack;


public class AuctionsPackSerializer extends JsonSerializer<AuctionsPack> {
    
    @Override
    public void serialize(
        AuctionsPack auctionsPack, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException  
    {
        gen.writeStartArray();
        for (Auction auction : auctionsPack.getAuctions()) {
            gen.writeStartObject();
            AuctionSerializerHelper.serializeBasics(gen, auction);
            AuctionSerializerHelper.serializeBidsData(gen, auction, false);
            AuctionSerializerHelper.serializeJustOnePictureUrl(gen, auction);
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}