package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter @AllArgsConstructor 
@Setter @NoArgsConstructor
public class AccountRegistrationRequest {

    private String name;
    private String surname;
    private String birthday;
    private String country;
    private String city;
    private String email;
    private String username;
    private String password;
}