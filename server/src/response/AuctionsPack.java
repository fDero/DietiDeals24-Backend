package response;

import java.util.List;
import entity.Auction;
import json.AuctionsPackSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = AuctionsPackSerializer.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class AuctionsPack {
    private List<Auction> auctions;
}