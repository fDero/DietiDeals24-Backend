package utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import response.SpecificAuctionPublicInformations;

public class SpecificAuctionPublicInformationsSerializer extends JsonSerializer<SpecificAuctionPublicInformations> {

    @Override
    public void serialize(
        SpecificAuctionPublicInformations value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    { 
        gen.writeStartObject();
        AuctionSerializerHelper.serializeBasics(gen, value.getAuction());
        AuctionSerializerHelper.serializeAllPicturesUrls(gen, value.getAuction());
        AuctionSerializerHelper.serializeDescription(gen, value.getAuction());
        gen.writeEndObject();
    }

}
