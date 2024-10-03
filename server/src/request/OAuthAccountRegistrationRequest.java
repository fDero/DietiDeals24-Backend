package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @AllArgsConstructor
@Setter @NoArgsConstructor
@ToString
public class OAuthAccountRegistrationRequest {
    private String name;
    private String surname;
    private String birthday;
    private String country;
    private String city;
    private String username;
    private String profilePictureUrl;
    private String oauthToken;
}
