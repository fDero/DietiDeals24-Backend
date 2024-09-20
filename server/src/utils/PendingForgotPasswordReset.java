package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @AllArgsConstructor
@Setter @NoArgsConstructor
@ToString
public class PendingForgotPasswordReset {
    
    private String email;
    private String username;
    private Integer accountId;
    private String authToken;
}
