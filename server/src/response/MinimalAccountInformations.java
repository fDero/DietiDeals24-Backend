package response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import entity.Account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.MinimalAccountInformationsSerializer;



@Getter @NoArgsConstructor
@Setter @AllArgsConstructor
@JsonSerialize(using = MinimalAccountInformationsSerializer.class)
public class MinimalAccountInformations {

    public MinimalAccountInformations(Account acount) {
        this.profilePictureUrl = acount.getProfilePictureUrl();
        this.email = acount.getEmail();
        this.username = acount.getUsername();
        this.unreadNotificationsCounter = acount.getUnreadNotificationsCounter();
    }

    private String  email; 
    private String  username; 
    private String  profilePictureUrl;
    private Integer unreadNotificationsCounter;
}
