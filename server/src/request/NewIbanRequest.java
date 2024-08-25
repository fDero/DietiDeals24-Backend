package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @NoArgsConstructor 
@Setter @AllArgsConstructor
public class NewIbanRequest extends NewPaymentMethodRequest {

    private String ibanString;
}
