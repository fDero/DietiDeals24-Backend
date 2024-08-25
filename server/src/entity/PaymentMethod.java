package entity;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = CreditCard.class, name = "CREDIT_CARD"),
    @JsonSubTypes.Type(value = Iban.class, name = "IBAN")
})
public class PaymentMethod {
    public String type;
}
