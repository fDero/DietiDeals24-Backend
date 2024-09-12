package request;

import java.math.BigDecimal;
import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @AllArgsConstructor
@Setter @NoArgsConstructor
public class NewAuctionRequest {
    
    private BigDecimal maximumBid;
    private BigDecimal minimumBid;
    private String country;
    private String city;
    private String itemCondition;
    private String itemCategory;
    private String macroCategory;
    private Timestamp endTime;
    private String itemName;
    private String description;
    private String[] picturesUrls;
    private String auctionType;
    private String currency;
}