package request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @AllArgsConstructor
@Setter @NoArgsConstructor
public class MessageForwardRequest {

    private String message;
    private Integer auctionId;
}
