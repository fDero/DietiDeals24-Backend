package response;

import java.util.List;
import entity.Activity;
import utils.UserPublicActivitySerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonSerialize(using = UserPublicActivitySerializer.class)
@Setter @Getter
public class UserPublicActivity {
    List<Activity> activity;

    public UserPublicActivity(List<Activity> activity) {
        System.out.println("UserPublicActivity");
        System.out.println("Activity size: " + activity.size());
        this.activity = activity;
    }
}
