package controller;

import entity.CreditCard;
import entity.Iban;
import exceptions.NoSuchPaymentMethodException;
import exceptions.PaymentMethodNotYoursException;
import request.NewPaymentMethodRequest;
import response.PaymentDescriptorsPack;
import service.PaymentProcessingService;

import org.springframework.http.ResponseEntity;
import java.util.List;
import authentication.JwtTokenManager;

import authentication.RequireJWT;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class PaymentController {

    private final JwtTokenManager jwtTokenProvider;
    private final PaymentProcessingService paymentManagementService;

    @Autowired
    public PaymentController(
        JwtTokenManager jwtTokenProvider,
        PaymentProcessingService paymentManagementService
    ){
        this.jwtTokenProvider = jwtTokenProvider;
        this.paymentManagementService = paymentManagementService;
    }

    @RequireJWT
    @GetMapping(value = "/payment-methods/list", produces = "application/json")
    public ResponseEntity<PaymentDescriptorsPack> sendPaymentMethods(
        @RequestHeader(name = "Authorization") String authorizationHeader
    ) {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String id = jwtTokenProvider.getIdFromJWT(jwtToken);
        List<Iban> ibans = paymentManagementService.fetchIbansByAccountId(Integer.valueOf(id));
        List<CreditCard> creditCards = paymentManagementService.fetchCreditCardsByAccountId(Integer.valueOf(id));
        PaymentDescriptorsPack paymentDescriptorsPack = new PaymentDescriptorsPack(creditCards, ibans);
        return ResponseEntity.ok().body(paymentDescriptorsPack);
    }

    @RequireJWT
    @PostMapping(value = "/payment-methods/new", produces = "text/plain")
    public ResponseEntity<String> createNewPaymentMethod(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestBody NewPaymentMethodRequest newPaymentMethodRequest
    ) {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String accountIdStr = jwtTokenProvider.getIdFromJWT(jwtToken);
        Integer accountId = Integer.valueOf(accountIdStr);
        paymentManagementService.savePaymentMethod(newPaymentMethodRequest, accountId);
        return ResponseEntity.ok().body("done");
    }

    @RequireJWT
    @DeleteMapping(value = "/payment-methods/delete", produces = "text/plain")
    public ResponseEntity<String> deleteIban(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestParam Integer paymentMethodId
    ) 
        throws 
            PaymentMethodNotYoursException, 
            NoSuchPaymentMethodException
    {
        String jwtToken = jwtTokenProvider.getTokenFromRequestHeader(authorizationHeader);
        String accountIdString = jwtTokenProvider.getIdFromJWT(jwtToken);
        Integer accountId = Integer.valueOf(accountIdString);
        paymentManagementService.deletePaymentMethod(paymentMethodId, accountId);
        return ResponseEntity.ok().body("done");
    }
}