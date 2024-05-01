package response;

import java.util.List;
import entity.Account;
import entity.ContactInformation;
import entity.PersonalLink;
import utils.AccountProfileInformationsSerializer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = AccountProfileInformationsSerializer.class)
@NoArgsConstructor
@AllArgsConstructor
@Data @Getter @Setter
public class AccountProfileInformations {
    Account account;
    List<PersonalLink> personalLinks;
    List<ContactInformation> contactInformation;
}