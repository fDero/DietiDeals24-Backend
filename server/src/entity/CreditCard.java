package entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import request.NewCreditCreditCardRequest;

@Entity @Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "CreditCard")
public class CreditCard  {

    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "last4digits")
    private String last4digits;

    @Column(name = "credit_card_token")
    private String creditCardToken;

    @Id @Column(name = "credit_card_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public CreditCard(NewCreditCreditCardRequest newCreditCreditCardRequest, Integer accountId) {
        this.accountId = accountId;
        this.last4digits = newCreditCreditCardRequest.getLast4digits();
        this.creditCardToken = newCreditCreditCardRequest.getCreditCardToken();
    }
}
