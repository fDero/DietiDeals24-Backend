package utils;

import request.AccountRegistrationRequest;
import lombok.*;
import java.sql.Timestamp;

import org.jetbrains.annotations.NotNull;


@NoArgsConstructor
@AllArgsConstructor
@Data @Getter
public class PendingAccountRegistration {

    private String name;
    private String surname;
    private Timestamp birthday;
    private String country;
    private String city;
    private String email;
    private String username;
    private String password;

    private String  confirmationCode;
    private Integer errorsCounter;

    public PendingAccountRegistration(
        @NotNull AccountRegistrationRequest request,
        @NotNull String confirmationCode
    ) {
        this.name = request.getName();
        this.surname = request.getSurname();
        this.birthday = TimestampFormatter.parseFromClientRequest(request.getBirthday());
        this.city = request.getCity();
        this.country = request.getCountry();
        this.username = request.getUsername();
        this.email = request.getEmail();
        this.password = request.getPassword();
        this.confirmationCode = confirmationCode;
        this.errorsCounter = 0;
    }

    public void incrementErrorsCounter(){
        errorsCounter = errorsCounter + 1;
    }

    public boolean hasTooManyErrors(){
        return this.errorsCounter >= 2;
    }
}
