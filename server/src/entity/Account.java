package entity;

import temporary.PendingAccountRegistration;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import java.sql.Date;

@Entity @Getter @Setter
public class Account {

    private String name;
    private String surname;
    private Date birthday;
    private String country;
    private String password;
    private String email;
    private String phone;

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Account(){
        this.setName(null);
        this.setSurname(null);
        this.setBirthday(null);
        this.setCountry(null);
        this.setPassword(null);
        this.setEmail(null);
        this.setPhone(null);
        this.setId(null);
    }

    public Account(@NotNull PendingAccountRegistration pending_account){
        name = pending_account.getName();
        surname = pending_account.getSurname();
        birthday = pending_account.getBirthday();
        country = pending_account.getCountry();
        password = pending_account.getPassword();
        email = pending_account.getEmail();
        phone = pending_account.getPhone();
        this.setId(null);
    }
}
