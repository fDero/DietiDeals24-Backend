package response;

import java.util.List;
import entity.Bid;
import json.BidsPackSerializer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;


@JsonSerialize(using = BidsPackSerializer.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
@ToString
public class BidsPack {

    private List<Bid> bids;
}