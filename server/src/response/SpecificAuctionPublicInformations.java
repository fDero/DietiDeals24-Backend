package response;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;

import utils.SpecificAuctionPublicInformationsSerializer;
import entity.Auction;

@JsonSerialize(using = SpecificAuctionPublicInformationsSerializer.class)
@Setter @Getter
public class SpecificAuctionPublicInformations {

    private Auction auction;

    public SpecificAuctionPublicInformations(Auction auction) {
        this.auction = auction;
    }
}
