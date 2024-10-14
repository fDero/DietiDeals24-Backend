package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter @AllArgsConstructor
@Getter @NoArgsConstructor
public class ForgotPasswordResetConfirmationRequest {

    private Integer userId;
    private String newPassword;
    private String authToken;
}
