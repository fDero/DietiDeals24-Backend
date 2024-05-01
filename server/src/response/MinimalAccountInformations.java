package response;

import entity.Account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter @NoArgsConstructor
@Setter @AllArgsConstructor
public class MinimalAccountInformations {

    public MinimalAccountInformations(Account acount) {
        this.profilePictureUrl = acount.getProfilePictureUrl();
        this.email = acount.getEmail();
        this.username = acount.getUsername();
        this.unreadNotificationsCounter = acount.getUnreadNotificationsCounter();
    }

    private String email; 
    private String username; 
    private String profilePictureUrl;
    private Long   unreadNotificationsCounter;
}
