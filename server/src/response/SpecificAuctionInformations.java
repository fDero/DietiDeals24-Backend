package response;

import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import entity.Auction;
import entity.Bid;
import json.SpecificAuctionInformationsSerializer;

@JsonSerialize(using = SpecificAuctionInformationsSerializer.class)
@NoArgsConstructor @Setter 
@AllArgsConstructor @Getter
public class SpecificAuctionInformations {

    private Auction auction;
    private Integer requesterId;
    private List<Bid> requesterBids;
}
