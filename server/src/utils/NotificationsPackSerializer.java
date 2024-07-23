package utils;

import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
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
        long nonReadCounter = 0;
        long notificationsCounter = value.getNotifications().size();
        gen.writeStartObject();
        gen.writeArrayFieldStart("notifications");
        for (Notification notification : value.getNotifications()) {
            gen.writeStartObject();
            gen.writeStringField("notificationType", notification.getNotificationType());
            gen.writeNumberField("auctionId", notification.getAuctionId());
            if (!notification.getVisualized()){
                nonReadCounter++;
            }
            gen.writeBooleanField("read", notification.getVisualized());
            gen.writeEndObject();
        }
        gen.writeEndArray();
        gen.writeNumberField("unreadNotifications", nonReadCounter);
        gen.writeNumberField("readNotifications", notificationsCounter - nonReadCounter);
        gen.writeNumberField("notificationsCounter", notificationsCounter);
        gen.writeEndObject();
    }
}