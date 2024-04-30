package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import entity.Account;


@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class AccountWithoutSensibleInformations {

    public AccountWithoutSensibleInformations(Account acount) {
        this.name = acount.getName();
        this.surname = acount.getSurname();
        this.birthday = acount.getBirthday();
        this.country = acount.getCountry();
        this.city = acount.getCity();
        this.email = acount.getEmail();
        this.username = acount.getUsername();
        this.unreadNotifications = acount.getUnreadNotifications();
    }

    private String name;
    private String surname;
    private Date   birthday;
    private String country;
    private String city; 
    private String email; 
    private String username; 
    private String profilePictureUrl;
    private Long   unreadNotifications;
}
