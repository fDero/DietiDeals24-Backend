package request;

import entity.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class NewBidRequest {
    private Integer auctionId;
    private Long bidAmount;
    private Integer paymentMethodId;
    private PaymentMethod OneTimePaymentMethod;
    private PaymentMethod paymentMethodToBeSaved;
}
