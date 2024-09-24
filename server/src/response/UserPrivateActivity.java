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

@JsonSerialize(using = UserPrivateActivity.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class UserPrivateActivity extends AuctionAwareJsonSerializer<UserPrivateActivity> {

    private List<Activity> activity;
    private Integer requesterId;

    @Override
    public void serialize(
        UserPrivateActivity userPrivateActivity,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        gen.writeStartArray();
        for (Activity activity : userPrivateActivity.getActivity()) {
            final Auction auction = new Auction(activity);
            final boolean isOwner = userPrivateActivity.getRequesterId() == auction.getCreatorId();
            gen.writeStartObject();
            serializeBasics(gen, auction);
            serializeBidsData(gen, auction, isOwner);
            serializeJustOnePictureUrl(gen, auction);
            if (!isOwner) {
                gen.writeNumberField("ownBid", activity.getBidAmount());
            }
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}
