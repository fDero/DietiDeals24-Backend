package utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import entity.Auction;
import entity.Notification;
import response.NotificationsPack;

public class NotificationsPackSerializer extends JsonSerializer<NotificationsPack> {
    
    @Override
    public void serialize(
        NotificationsPack value, 
        JsonGenerator gen, 
        SerializerProvider serializers
    ) 
        throws IOException  
    {
        gen.writeStartObject();
        gen.writeArrayFieldStart("notifications");
        for (Notification notification : value.getNotifications()) {
            gen.writeStartObject();
            gen.writeNumberField("id", notification.getId());
            gen.writeStringField("notificationType", notification.getNotificationType());
            gen.writeBooleanField("read", notification.getVisualized());
            gen.writeObjectFieldStart("auction");
            Auction auction = notification.toAuction();
            AuctionSerializerHelper.serializeBasics(gen, auction);
            AuctionSerializerHelper.serializeJustOnePictureUrl(gen, auction);
            gen.writeEndObject();
            gen.writeEndObject();
        }
        gen.writeEndArray();
        long unreadNotificationsCounter = value.getUnreadNotificationsCounter();
        long readNotificationsCounter = value.getReadNotificationsCounter();
        long notificationsCounter = readNotificationsCounter + unreadNotificationsCounter;
        gen.writeNumberField("unreadNotifications", unreadNotificationsCounter);
        gen.writeNumberField("readNotifications", readNotificationsCounter);
        gen.writeNumberField("notificationsCounter", notificationsCounter);
        gen.writeEndObject();
    }
}