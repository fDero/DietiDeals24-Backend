package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @AllArgsConstructor 
@Setter @NoArgsConstructor
public class ProfileUpdateRequest {

    private String newBio;
    private String newCountry;
    private String newCity;
    private String newEmail;
    private String newUsername;
}