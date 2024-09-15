package exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import request.NewPaymentMethodRequest;

@AllArgsConstructor @Getter
@NoArgsConstructor @Setter
@ToString
public class AuctionFinalizationRequest {
    
    private Integer auctionId;
    private String choice;
    private Integer paymentMethodId;
    private NewPaymentMethodRequest OneTimePaymentMethod;
    private NewPaymentMethodRequest paymentMethodToBeSaved;
}
