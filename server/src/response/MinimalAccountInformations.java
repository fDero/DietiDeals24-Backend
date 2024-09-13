package response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import entity.Account;
import json.MinimalAccountInformationsSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@JsonSerialize(using = MinimalAccountInformationsSerializer.class)
@AllArgsConstructor @Getter 
@NoArgsConstructor @Setter
public class MinimalAccountInformations {
    
    private Account account;
}
