package json;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import entity.Auction;
import response.SpecificAuctionPublicInformations;

public class SpecificAuctionPublicInformationsSerializer extends JsonSerializer<SpecificAuctionPublicInformations> {

    @Override
    public void serialize(
        SpecificAuctionPublicInformations specificAuctionPublicInformations, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    { 
        final Auction auction = specificAuctionPublicInformations.getAuction();
        gen.writeStartObject();
        AuctionSerializerHelper.serializeBasics(gen, auction);
        AuctionSerializerHelper.serializeBidsData(gen, auction);
        AuctionSerializerHelper.serializeAllPicturesUrls(gen, auction);
        AuctionSerializerHelper.serializeDescription(gen, auction);
        gen.writeEndObject();
    }

}
