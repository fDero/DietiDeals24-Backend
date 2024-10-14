package service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import entity.Auction;
import entity.CreditCard;
import entity.Iban;
import request.AuctionClosingRequest;
import exceptions.MissingPaymentMethodException;
import exceptions.NoSuchPaymentMethodException;
import exceptions.PaymentMethodNotYoursException;
import exceptions.NoAuctionWithSuchIdException;
import repository.AuctionRepository;
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
    private final AuctionRepository auctionRepository;

    @Autowired
    public PaymentProcessingService(
        IbanRepository ibanRepository,
        CreditCardRepository creditCardRepository,
        AuctionRepository auctionRepository
    ) {
        this.ibanRepository = ibanRepository;
        this.creditCardRepository = creditCardRepository;
        this.auctionRepository = auctionRepository;
    }

    public void saveIban(Iban iban) {
        ibanRepository.save(iban);
    }

    public void saveCreditCard(entity.CreditCard creditCard) {
        creditCardRepository.save(creditCard);
    }

    public void deleteIban(Iban iban) {
        ibanRepository.delete(iban);
    }

    public void deleteCreditCard(entity.CreditCard creditCard) {
        creditCardRepository.delete(creditCard);
    }

    public void savePaymentMethod(NewPaymentMethodRequest paymentMethodRequest, Integer accountId) {
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

    public List<Iban> fetchIbansByAccountId(Integer accountId) {
        return ibanRepository.findByAccountId(accountId);
    }

    public List<CreditCard> fetchCreditCardsByAccountId(Integer accountId) {
        return creditCardRepository.findByAccountId(accountId);
    }

    public String processBidPayment(NewBidRequest newBidRequest, Integer bidderId) 
        throws 
            NoAuctionWithSuchIdException, 
            NoSuchPaymentMethodException, 
            PaymentMethodNotYoursException, 
            MissingPaymentMethodException 
    {
        final Optional<Auction> auction = auctionRepository.findById(newBidRequest.getAuctionId());
        if (auction.isEmpty()) {
            throw new NoAuctionWithSuchIdException();
        }
        final boolean isOneTimePaymentMethod = newBidRequest.getOneTimePaymentMethod() != null;
        final boolean isPaymentMethodToBeSaved = newBidRequest.getPaymentMethodToBeSaved() != null;
        final boolean isNewPaymentMethod = isOneTimePaymentMethod || isPaymentMethodToBeSaved;
        final boolean isExistingPaymentMethod = newBidRequest.getPaymentMethodId() != null;
        if (isExistingPaymentMethod) {
            return processBidPaymentWithExistingPaymentMethod(newBidRequest.getPaymentMethodId(), bidderId);
        }
        if(isPaymentMethodToBeSaved) {
            savePaymentMethod(newBidRequest.getPaymentMethodToBeSaved(), bidderId);
        }
        if (isNewPaymentMethod) {    
            return processBidPaymentWithNewPaymentMethod(newBidRequest);
        }
        throw new MissingPaymentMethodException();
    }

    public String processBidPaymentWithExistingPaymentMethod(Integer paymentMethodId, Integer bidderId)
        throws 
            NoSuchPaymentMethodException,
            PaymentMethodNotYoursException
    {
        final Iban iban = ibanRepository.findById(paymentMethodId).orElse(null);
        final CreditCard creditCard = creditCardRepository.findById(paymentMethodId).orElse(null);
        final boolean isIban = iban != null;
        final boolean isCreditCard = creditCard != null;
        final boolean isCorrect = isIban || isCreditCard;
        if (!isCorrect) {
            throw new NoSuchPaymentMethodException();
        }
        if (isIban && !iban.getAccountId().equals(bidderId)) {
            throw new PaymentMethodNotYoursException();
        }
        if (isCreditCard && !creditCard.getAccountId().equals(bidderId)) {
            throw new PaymentMethodNotYoursException();
        }
        return isIban ? iban.getIbanString() : "<<dummy-credit-card-refound-token>>";
    }

    public String processBidPaymentWithNewPaymentMethod(NewBidRequest newBidRequest)
        throws 
            MissingPaymentMethodException 
    {
        final boolean isOneTimePaymentMethod = newBidRequest.getOneTimePaymentMethod() != null;
        final boolean isPaymentMethodToBeSaved = newBidRequest.getPaymentMethodToBeSaved() != null;
        assert isOneTimePaymentMethod || isPaymentMethodToBeSaved;
        NewPaymentMethodRequest paymentMethodRequest = isOneTimePaymentMethod ? 
            newBidRequest.getOneTimePaymentMethod() : newBidRequest.getPaymentMethodToBeSaved();
        if (paymentMethodRequest instanceof NewCreditCreditCardRequest) {
            return "<<dummy-credit-card-refound-token>>";
        } else if (paymentMethodRequest instanceof NewIbanRequest newIbanRequest) {
            return newIbanRequest.getIbanString();
        }
        else {
            throw new MissingPaymentMethodException();
        }
    }

    public void processAuctionClosingPayment(Auction auction, AuctionClosingRequest auctionFinalizationRequest) 
        throws 
            NoSuchPaymentMethodException, 
            PaymentMethodNotYoursException, 
            MissingPaymentMethodException 
    {
        final boolean isOneTimePaymentMethod = auctionFinalizationRequest.getOneTimePaymentMethod() != null;
        final boolean isPaymentMethodToBeSaved = auctionFinalizationRequest.getPaymentMethodToBeSaved() != null;
        final boolean isNewPaymentMethod = isOneTimePaymentMethod || isPaymentMethodToBeSaved;
        final boolean isExistingPaymentMethod = auctionFinalizationRequest.getPaymentMethodId() != null;
        if (!isNewPaymentMethod && !isExistingPaymentMethod) {
            throw new MissingPaymentMethodException();
        }
        if (isExistingPaymentMethod) {
            processAuctionClosingPaymentWithExistingPaymentMethod(auctionFinalizationRequest.getPaymentMethodId(), auction);
        }
        if(isPaymentMethodToBeSaved) {
            savePaymentMethod(auctionFinalizationRequest.getPaymentMethodToBeSaved(), auction.getCreatorId());
        }
        if (isNewPaymentMethod) {    
            processAuctionClosingPaymentWithNewPaymentMethod(auctionFinalizationRequest);
        }
    }

    public void processAuctionClosingPaymentWithExistingPaymentMethod(Integer paymentMethodId, Auction auction) 
        throws 
            PaymentMethodNotYoursException, 
            NoSuchPaymentMethodException 
    {
        final Iban iban = ibanRepository.findById(paymentMethodId).orElse(null);
        final CreditCard creditCard = creditCardRepository.findById(paymentMethodId).orElse(null);
        final boolean isIban = iban != null;
        final boolean isCreditCard = creditCard != null;
        final boolean isCorrect = isIban || isCreditCard;
        if (!isCorrect) {
            throw new NoSuchPaymentMethodException();
        }
        if (isIban && !iban.getAccountId().equals(auction.getCreatorId())) {
            throw new PaymentMethodNotYoursException();
        }
        if (isCreditCard && !creditCard.getAccountId().equals(auction.getCreatorId())) {
            throw new PaymentMethodNotYoursException();
        }
    }

    public void processAuctionClosingPaymentWithNewPaymentMethod(AuctionClosingRequest auctionFinalizationRequest)
        throws 
            NoSuchPaymentMethodException
    {
        final boolean isOneTimePaymentMethod = auctionFinalizationRequest.getOneTimePaymentMethod() != null;
        final boolean isPaymentMethodToBeSaved = auctionFinalizationRequest.getPaymentMethodToBeSaved() != null;
        assert isOneTimePaymentMethod || isPaymentMethodToBeSaved;
        NewPaymentMethodRequest paymentMethodRequest = isOneTimePaymentMethod ? 
            auctionFinalizationRequest.getOneTimePaymentMethod() : auctionFinalizationRequest.getPaymentMethodToBeSaved();
        final boolean isCreditCard = paymentMethodRequest instanceof NewCreditCreditCardRequest;
        final boolean isIban = paymentMethodRequest instanceof NewIbanRequest;
        if (!isCreditCard && !isIban) {
            throw new NoSuchPaymentMethodException();
        }
    }

    public void deletePaymentMethod(Integer paymentMethodId, Integer accountId) 
        throws 
            NoSuchPaymentMethodException,
            PaymentMethodNotYoursException 
    {
        final Iban iban = ibanRepository.findById(paymentMethodId).orElse(null);
        final CreditCard creditCard = creditCardRepository.findById(paymentMethodId).orElse(null);
        final boolean isIban = iban != null;
        final boolean isCreditCard = creditCard != null;
        final boolean isCorrect = isIban || isCreditCard;
        if (!isCorrect) {
            throw new NoSuchPaymentMethodException();
        }
        if (isIban && !iban.getAccountId().equals(accountId)) {
            throw new PaymentMethodNotYoursException();
        }
        if (isCreditCard && !creditCard.getAccountId().equals(accountId)) {
            throw new PaymentMethodNotYoursException();
        }
        if (isIban) {
            deleteIban(iban);
        }
        if (isCreditCard) {
            deleteCreditCard(creditCard);
        }
    }
}
