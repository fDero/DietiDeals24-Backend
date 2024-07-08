package utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Auction;
import response.AuctionsPack;
import response.SpecificAuctionPublicInformations;


public class AuctionsPackSerializer extends JsonSerializer<AuctionsPack> {
    
    @Override
    public void serialize(
        AuctionsPack value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException  
    {
        gen.writeStartArray();
        SpecificAuctionPublicInformationsSerializer serializer = new SpecificAuctionPublicInformationsSerializer();
        for (Auction auction : value.getAuctions()) {
            SpecificAuctionPublicInformations specificAuctionsPublicInformations = new SpecificAuctionPublicInformations(auction);
            serializer.serialize(specificAuctionsPublicInformations, gen, serializers);
        }
        gen.writeEndArray();
    }
}