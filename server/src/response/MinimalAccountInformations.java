package response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import entity.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.MinimalAccountInformationsSerializer;


@JsonSerialize(using = MinimalAccountInformationsSerializer.class)
@AllArgsConstructor @Getter 
@NoArgsConstructor @Setter
public class MinimalAccountInformations {
    
    private Account account;
}
