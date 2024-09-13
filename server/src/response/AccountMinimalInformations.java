package response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import entity.Account;
import json.AccountMinimalInformationsSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonSerialize(using = AccountMinimalInformationsSerializer.class)
@AllArgsConstructor @Getter 
@NoArgsConstructor @Setter
public class AccountMinimalInformations {
    
    private Account account;
}
