package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @AllArgsConstructor
@Setter @NoArgsConstructor
@ToString
public class OAuthTokenWrapperRequest {
    
    private String oauthToken;
}
