package response;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Activity;
import entity.Auction;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.AuctionAwareJsonSerializer;

@JsonSerialize(using = UserPublicActivity.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class UserPublicActivity extends AuctionAwareJsonSerializer<UserPublicActivity> {

    private List<Activity> activities;

    @Override
    public void serialize(
        UserPublicActivity userPublicActivity,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        gen.writeStartArray();
        for (Activity activity : userPublicActivity.getActivities()) {
            Auction auction = new Auction(activity);
            gen.writeStartObject();
            serializeBasics(gen, auction);
            serializeBidsData(gen, auction, false);
            serializeJustOnePictureUrl(gen, auction);
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}
