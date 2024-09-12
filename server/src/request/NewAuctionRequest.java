package request;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @AllArgsConstructor
@Setter @NoArgsConstructor
@ToString
public class NewAuctionRequest {
    
    private BigDecimal maximumBid;
    private BigDecimal minimumBid;
    private String country;
    private String city;
    private String itemCondition;
    private String itemCategory;
    private String macroCategory;
    private String endTime;
    private String itemName;
    private String description;
    private String[] picturesUrls;
    private String auctionType;
    private String currency;
}