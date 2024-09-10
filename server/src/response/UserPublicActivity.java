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
@AllArgsConstructor
@NoArgsConstructor
public class UserPublicActivity {
    List<Activity> activity;
}
