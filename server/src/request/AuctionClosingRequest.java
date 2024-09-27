package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor @Getter
@NoArgsConstructor @Setter
@ToString
public class AuctionClosingRequest {
    
    private Integer auctionId;
    private Integer paymentMethodId;
    private NewPaymentMethodRequest oneTimePaymentMethod;
    private NewPaymentMethodRequest paymentMethodToBeSaved;
}
