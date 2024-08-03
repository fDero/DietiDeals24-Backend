package response;

import java.util.List;

import entity.CreditCard;
import entity.Iban;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import utils.PaymentDescriptorsPackSerializer;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = PaymentDescriptorsPackSerializer.class)
@Setter @NoArgsConstructor
@Getter @AllArgsConstructor
public class PaymentDescriptorsPack {

    private List<CreditCard> creditCards;
    private List<Iban> ibans;
}
