package response;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import utils.AuctionAwareJsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import entity.Auction;
import entity.Notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = NotificationsPack.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class NotificationsPack extends AuctionAwareJsonSerializer<NotificationsPack> {

    private List<Notification> notifications;
    private long readNotificationsCounter;
    private long unreadNotificationsCounter;

    @Override
    public void serialize(
        NotificationsPack notificationsPack,
        JsonGenerator gen,
        SerializerProvider serializers
    )
        throws
            IOException
    {
        gen.writeStartObject();
        gen.writeArrayFieldStart("notifications");
        for (Notification notification : notificationsPack.getNotifications()) {
            gen.writeStartObject();
            gen.writeNumberField("id", notification.getId());
            gen.writeStringField("notificationType", notification.getNotificationType());
            gen.writeBooleanField("read", notification.getVisualized());
            gen.writeObjectFieldStart("auction");
            Auction auction = notification.toAuction();
            serializeBasics(gen, auction);
            serializeBidsData(gen, auction, false);
            serializeJustOnePictureUrl(gen, auction);
            gen.writeEndObject();
            gen.writeEndObject();
        }
        gen.writeEndArray();
        long notificationsCounter = notificationsPack.getReadNotificationsCounter();
        notificationsCounter += notificationsPack.getUnreadNotificationsCounter();
        gen.writeNumberField("unreadNotifications", notificationsPack.getUnreadNotificationsCounter());
        gen.writeNumberField("readNotifications", notificationsPack.getReadNotificationsCounter());
        gen.writeNumberField("notificationsCounter", notificationsCounter);
        gen.writeEndObject();
    }
}
