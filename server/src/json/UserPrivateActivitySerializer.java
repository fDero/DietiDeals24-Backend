package json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import entity.Activity;
import entity.Auction;
import response.UserPrivateActivity;

public class UserPrivateActivitySerializer extends JsonSerializer<UserPrivateActivity> {

    @Override
    public void serialize(
        UserPrivateActivity userPrivateActivity, 
        JsonGenerator gen, 
        SerializerProvider serializers
    )
        throws IOException 
    {
        gen.writeStartArray();
        for (Activity activity : userPrivateActivity.getActivity()) {
            Auction auction = new Auction(activity);
            gen.writeStartObject();
            AuctionSerializerHelper.serializeBasics(gen, auction);
            AuctionSerializerHelper.serializeBidsData(gen, auction);
            AuctionSerializerHelper.serializeJustOnePictureUrl(gen, auction);
            if (userPrivateActivity.getRequesterId() != auction.getCreatorId()) {
                gen.writeNumberField("ownBid", activity.getBidAmount());
            }
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}
