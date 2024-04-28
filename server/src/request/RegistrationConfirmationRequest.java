package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class RegistrationConfirmationRequest {

    private String email;
    private String confirmation_code;
}
