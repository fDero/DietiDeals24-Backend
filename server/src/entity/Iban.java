package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import request.NewIbanRequest;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "Iban")
public class Iban {

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "iban_string")
    private String ibanString;

    @Id @Column(name = "iban_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    public Iban(NewIbanRequest ibanRequest, Integer accountId) {
        this.ibanString = ibanRequest.getIbanString();
        this.accountId = accountId;
    }
}
