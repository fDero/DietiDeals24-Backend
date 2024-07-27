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
            gen.writeStartObject();
            gen.writeObjectFieldStart("auction");

            gen.writeNumberField("auctionId", notification.getAuctionId());
            gen.writeStringField("id", notification.getId().toString());
            gen.writeStringField("title", notification.getItemName());
            gen.writeStringField("country", notification.getCountry());
            gen.writeStringField("city", notification.getCity());
            gen.writeStringField("conditions", notification.getItemCondition());
            gen.writeStringField("type", notification.getAuctionType());
            if (notification.getAuctionType().equals("silent")) {
                gen.writeNumberField("minimumBid", notification.getMinimumBid());
            } 
            else if (notification.getAuctionType().equals("reverse")) {
                gen.writeNumberField("maximumBid", notification.getMaximumBid());
                gen.writeNumberField("lowestBidSoFar", notification.getLowestBidSoFar());
            }
            gen.writeStringField("endTime", notification.getEndTime().toString()); 
            gen.writeArrayFieldStart("picturesUrls");
            for (String pictureUrl : notification.getPicturesUrls()){
                gen.writeString(pictureUrl);
            }
            gen.writeEndArray();
            gen.writeStringField("status", "ACTIVE");
            gen.writeStringField("currency", notification.getCurrency());
            gen.writeEndObject();

            gen.writeEndObject();
            if (!notification.getVisualized()){
                nonReadCounter++;
            }
            gen.writeBooleanField("read", notification.getVisualized());
            
        }
        gen.writeEndArray();
        gen.writeNumberField("unreadNotifications", nonReadCounter);
        gen.writeNumberField("readNotifications", notificationsCounter - nonReadCounter);
        gen.writeNumberField("notificationsCounter", notificationsCounter);
        gen.writeEndObject();
    }
}