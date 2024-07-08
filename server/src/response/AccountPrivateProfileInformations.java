package response;

import java.util.List;
import entity.Account;
import entity.ContactInformation;
import entity.PersonalLink;
import utils.AccountPrivateProfileInformationsSerializer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = AccountPrivateProfileInformationsSerializer.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class AccountPrivateProfileInformations {
    Account account;
    List<PersonalLink> personalLinks;
    List<ContactInformation> contactInformation;
}