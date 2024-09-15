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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "payment_methods_ids")
    @SequenceGenerator(name = "payment_methods_seq", sequenceName = "payment_methods_ids", allocationSize = 50, initialValue = 1)
    private Integer id;
    
    public Iban(NewIbanRequest ibanRequest, Integer accountId) {
        this.ibanString = ibanRequest.getIbanString();
        this.accountId = accountId;
    }
}
