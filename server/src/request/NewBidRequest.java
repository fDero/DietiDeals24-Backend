package request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @Getter 
@NoArgsConstructor @Setter
public class NewBidRequest {

    private Integer auctionId;
    private BigDecimal bidAmount;
    private Integer paymentMethodId;
    private NewPaymentMethodRequest oneTimePaymentMethod;
    private NewPaymentMethodRequest paymentMethodToBeSaved;
}
