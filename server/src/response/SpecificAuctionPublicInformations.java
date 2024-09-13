package response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Getter;
import lombok.Setter;
import entity.Auction;
import json.SpecificAuctionPublicInformationsSerializer;

@JsonSerialize(using = SpecificAuctionPublicInformationsSerializer.class)
@Setter @Getter
public class SpecificAuctionPublicInformations {

    private Auction auction;

    public SpecificAuctionPublicInformations(Auction auction) {
        this.auction = auction;
    }
}
