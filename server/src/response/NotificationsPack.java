package response;

import java.util.List;
import entity.Notification;
import utils.NotificationsPackSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = NotificationsPackSerializer.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class NotificationsPack {
    private List<Notification> notifications;
}
