package request;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @NoArgsConstructor 
@Setter @AllArgsConstructor
public class NewCreditCreditCardRequest extends NewPaymentMethodRequest {

    private String cardNumber;
    private Date   expirationDate;
    private String name;
    private String address;
    private String city;
    private String country;
    private String zip;
}
