package request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME, 
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = NewCreditCreditCardRequest.class, name = "CREDIT_CARD"),
    @JsonSubTypes.Type(value = NewIbanRequest.class, name = "IBAN")
})
public interface NewPaymentMethodRequest { }
