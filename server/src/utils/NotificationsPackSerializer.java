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
        gen.writeStartArray();
        for (Notification notification : value.getNotifications()) {
            gen.writeStartObject();
            gen.writeStringField("notificationType", notification.getNotificationType());
            if (notification.getSilentAuctionId() != null){
                gen.writeNumberField("auctionId", notification.getSilentAuctionId());
            }
            if (notification.getReverseAuctionId() != null){
                gen.writeNumberField("auctionId", notification.getReverseAuctionId());
            }
            gen.writeBooleanField("read", notification.getVisualized());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}