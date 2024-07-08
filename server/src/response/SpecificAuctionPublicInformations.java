package response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import utils.SpecificAuctionPublicInformationsSerializer;
import entity.Auction;

@JsonSerialize(using = SpecificAuctionPublicInformationsSerializer.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class SpecificAuctionPublicInformations {
    private Auction auction;
}
