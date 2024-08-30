package request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter @NoArgsConstructor @AllArgsConstructor
public class NewBidRequest {
    private Integer auctionId;
    private BigDecimal bidAmount;
}
