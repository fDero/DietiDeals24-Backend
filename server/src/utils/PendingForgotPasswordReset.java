package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @AllArgsConstructor
@Setter @ToString
public class PendingForgotPasswordReset {
    
    private final String email;
    private final String username;
    private final Integer accountId;
    private final String authToken;
}
