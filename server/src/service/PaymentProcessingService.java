package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.CreditCard;
import entity.Iban;
import repository.CreditCardRepository;
import repository.IbanRepository;
import request.NewBidRequest;
import request.NewCreditCreditCardRequest;
import request.NewIbanRequest;
import request.NewPaymentMethodRequest;

@Service
public class PaymentProcessingService {
    
    private final IbanRepository ibanRepository;
    private final CreditCardRepository creditCardRepository;

    @Autowired
    public PaymentProcessingService(
        IbanRepository ibanRepository,
        CreditCardRepository creditCardRepository
    ) {
        this.ibanRepository = ibanRepository;
        this.creditCardRepository = creditCardRepository;
    }

    public void saveIban(entity.Iban iban) {
        ibanRepository.save(iban);
    }

    public void saveCreditCard(entity.CreditCard creditCard) {
        creditCardRepository.save(creditCard);
    }

    public void deleteIban(entity.Iban iban) {
        ibanRepository.delete(iban);
    }

    public void deleteCreditCard(entity.CreditCard creditCard) {
        creditCardRepository.delete(creditCard);
    }

    public void savePaymentMethod(NewPaymentMethodRequest paymentMethodRequest, String accountIdStr) {
        Integer accountId = Integer.valueOf(accountIdStr);
        if (paymentMethodRequest instanceof NewCreditCreditCardRequest creditCardRequest) {
            CreditCard creditCard = new CreditCard(creditCardRequest, accountId);
            saveCreditCard(creditCard);
        } else {
            assert paymentMethodRequest instanceof NewIbanRequest;
            NewIbanRequest ibanRequest = (NewIbanRequest) paymentMethodRequest;
            Iban iban = new Iban(ibanRequest, accountId);
            saveIban(iban);
        }
    }

    public String doPayment(NewBidRequest newBidRequest, String accountId) {
        if (newBidRequest.getPaymentMethodToBeSaved() != null) {
            savePaymentMethod(newBidRequest.getPaymentMethodToBeSaved(), accountId);
        }
        return "dummy token";
    }
}
