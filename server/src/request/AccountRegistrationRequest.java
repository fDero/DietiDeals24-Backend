package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Getter @AllArgsConstructor @NoArgsConstructor
public class AccountRegistrationRequest {

    private String name;
    private String surname;
    private Date   birthday;
    private String country;
    private String city;
    private String email;
    private String username;
    private String password;
}