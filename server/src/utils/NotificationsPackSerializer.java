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
                gen.writeNumberField("silentAuctionId", notification.getSilentAuctionId());
            }
            if (notification.getReverseAuctionId() != null){
                gen.writeNumberField("reverseAuctionId", notification.getReverseAuctionId());
            }
            gen.writeBooleanField("visualized", notification.getVisualized());
            gen.writeEndObject();
        }
        gen.writeEndArray();
    }
}