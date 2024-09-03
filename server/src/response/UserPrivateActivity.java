package response;

import java.util.List;
import entity.Activity;
import utils.UserPrivateActivitySerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonSerialize(using = UserPrivateActivitySerializer.class)
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPrivateActivity {
    List<Activity> activity;
}
