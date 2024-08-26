package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @NoArgsConstructor 
@Setter @AllArgsConstructor
public class NewCreditCreditCardRequest implements NewPaymentMethodRequest {

    private String type;
    private String paymentProcessorToken;
    private Integer last4digits;
}
