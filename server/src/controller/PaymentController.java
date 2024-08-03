package controller;


import entity.CreditCard;
import entity.Iban;
import repository.CreditCardRepository;
import repository.IbanRepository;
import response.PaymentDescriptorsPack;

import org.springframework.http.ResponseEntity;

import java.util.List;
import authentication.JwtTokenManager;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import authentication.RequireJWT;


@RestController
public class PaymentController {
    
    private final IbanRepository ibanRepository;
    private final CreditCardRepository creditCardRepository;
    private final JwtTokenManager jwtTokenProvider;
    
    @Autowired
    public PaymentController(
        IbanRepository ibanRepository,
        CreditCardRepository creditCardRepository,
        JwtTokenManager jwtTokenProvider
    ) {
        this.ibanRepository = ibanRepository;
        this.creditCardRepository = creditCardRepository;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @RequireJWT
    @Transactional
    @GetMapping(value = "/payments/methods", produces = "application/json")
    public ResponseEntity<PaymentDescriptorsPack> sendPrivateProfileInformations(@RequestHeader(name = "Authorization") String authorizationHeader) {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenProvider.getIdFromJWT(jwtToken);
        List<Iban> ibans = ibanRepository.findByAccountId(Integer.valueOf(id));
        List<CreditCard> creditCards = creditCardRepository.findByAccountId(Integer.valueOf(id));
        PaymentDescriptorsPack paymentDescriptorsPack = new PaymentDescriptorsPack(creditCards, ibans);
        return ResponseEntity.ok().body(paymentDescriptorsPack);
    }
}
