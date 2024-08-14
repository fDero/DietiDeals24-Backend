package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class NewBidRequest {
    private Integer auctionId;
    private Long bidAmount;
    private Integer paymentMethodId;
    private NewCreditCardRequest OneTimePaymentMethod;
    private NewCreditCardRequest paymentMethodToBeSaved;
}
