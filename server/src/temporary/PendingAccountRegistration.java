package temporary;

import request.AccountRegistrationRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.jetbrains.annotations.NotNull;
import java.sql.Date;

@NoArgsConstructor
@AllArgsConstructor
@Data @Getter
public class PendingAccountRegistration {

    private String  name;
    private String  surname;
    private Date    birthday;
    private String  country;
    private String  password;
    private String  email;
    private String  phone;

    private String  confirmation_code;
    private Integer errors_counter;

    public PendingAccountRegistration(
            @NotNull AccountRegistrationRequest request,
            @NotNull String confirmation_code
    ) {
        this.name = request.getName();
        this.surname = request.getSurname();
        this.birthday = request.getBirthday();
        this.country = request.getCountry();
        this.password = request.getPassword();
        this.email = request.getEmail();
        this.phone = request.getPhone();
        this.confirmation_code = confirmation_code;
        this.errors_counter = 0;
    }

    public void incrementErrorsCounter(){
        errors_counter = errors_counter + 1;
    }

    public static PendingAccountRegistration jsonDecode(@NotNull String json_encoded_object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json_encoded_object, PendingAccountRegistration.class);

    }

    public static String jsonEncode(@NotNull PendingAccountRegistration plain_object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(plain_object);
    }
}
