package service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.CreditCard;
import entity.Iban;
import exceptions.CreditCardNotFoundException;
import exceptions.CreditCardNotYoursException;
import exceptions.IbanNotFoundException;
import exceptions.IbanNotYoursException;
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

    public List<Iban> fetchIbansByAccountId(Integer accountId) {
        return ibanRepository.findByAccountId(accountId);
    }

    public List<CreditCard> fetchCreditCardsByAccountId(Integer accountId) {
        return creditCardRepository.findByAccountId(accountId);
    }

    public void deleteIban(Integer ibanId, Integer accountId) 
        throws 
            IbanNotFoundException, 
            IbanNotYoursException
    {
        Iban iban = ibanRepository.findById(ibanId).orElseThrow(
            () -> new IbanNotFoundException());
        if (!iban.getAccountId().equals(accountId)) {
            throw new IbanNotYoursException();
        }
        ibanRepository.deleteById(ibanId);
    }

    public void deleteCreditCard(Integer creditCardId, Integer accountId) 
        throws 
            CreditCardNotFoundException, 
            CreditCardNotYoursException
    {
        Iban iban = ibanRepository.findById(creditCardId).orElseThrow(
            () -> new CreditCardNotFoundException());
        if (!iban.getAccountId().equals(accountId)) {
            throw new CreditCardNotYoursException();
        }
        creditCardRepository.deleteById(creditCardId);
    }
}
