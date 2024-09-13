package controller;

import entity.Account;
import repository.PasswordRepository;
import request.AccountRegistrationRequest;
import request.RegistrationConfirmationRequest;
import response.AccountMinimalInformations;
import service.AccountManagementService;
import service.AccountValidationService;
import service.PendingAccountRegistrationCacheService;
import service.EmailService;
import service.EncryptionService;
import utils.PendingAccountRegistration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import authentication.JwtTokenManager;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountValidationException;
import exceptions.NoPendingAccountConfirmationException;
import exceptions.TooManyConfirmationCodes;
import exceptions.UnrecognizedCityException;
import exceptions.UnrecognizedCountryException;
import exceptions.WrongConfirmationCodeException;

@Transactional
@RestController
public class RegistrationController {

    private final AccountValidationService accountValidationService;
    private final AccountManagementService accountManagementService;
    private final EmailService emailService;
    private final PendingAccountRegistrationCacheService pendingAccountsCacheService;
    private final JwtTokenManager jwtTokenProvider;
    
    @Autowired
    public RegistrationController(
        EmailService emailService,
        AccountManagementService accountManagementService,
        AccountValidationService accountValidationService,
        PendingAccountRegistrationCacheService pendingAccountsCacheService,
        EncryptionService encryptionService,
        JwtTokenManager jwtTokenProvider,
        PasswordRepository passwordRepository
    ) {
        this.emailService = emailService;
        this.accountManagementService = accountManagementService;
        this.accountValidationService = accountValidationService;
        this.pendingAccountsCacheService = pendingAccountsCacheService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @NotNull
    private String generateConfirmationCode(){
        Random random = new Random();
        StringBuilder randomGeneratedCode = new StringBuilder();
        for (int i = 0; i < 5; i++)
            randomGeneratedCode.append(random.nextInt(10));
        return randomGeneratedCode.toString();
    }

    @PostMapping(value = "/register/init", produces = "text/plain")
    public ResponseEntity<String> initializeRegistration(@RequestBody @NotNull AccountRegistrationRequest request) 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException,
            UnrecognizedCityException, 
            UnrecognizedCountryException 
    {
        accountValidationService.validateAccountRegistrationRequest(request);
        String confirmationCode = this.generateConfirmationCode();
        PendingAccountRegistration registrationData = new PendingAccountRegistration(request, confirmationCode);
        pendingAccountsCacheService.store(registrationData,10);
        emailService.sendRegistrationConfirmEmail(request.getEmail(), confirmationCode);
        return ResponseEntity.ok().body("an email was sent to: " + request.getEmail());
    }

    @PostMapping(value = "/register/confirm", produces = "application/json")
    public ResponseEntity<AccountMinimalInformations> confirmRegistration(@RequestBody @NotNull RegistrationConfirmationRequest request) 
        throws 
            NoPendingAccountConfirmationException, 
            WrongConfirmationCodeException, 
            TooManyConfirmationCodes 
    {
        PendingAccountRegistration pendingAccount = pendingAccountsCacheService.retrieve(request.getEmail());
        ensureValidConfirmationCode(pendingAccount, request.getCode());
        Account account = accountManagementService.createAccount(pendingAccount);
        pendingAccountsCacheService.delete(request.getEmail());
        AccountMinimalInformations accountView = new AccountMinimalInformations(account);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", jwtTokenProvider.generateToken(account.getId().toString()));
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("X-Auth-Token");
        headers.setAccessControlExposeHeaders(allowedHeaders);
        return ResponseEntity.ok().headers(headers).body(accountView);    
    }

    public void ensureValidConfirmationCode(PendingAccountRegistration pendingAccount, String confirmationCode)
        throws 
            NoPendingAccountConfirmationException, 
            WrongConfirmationCodeException, 
            TooManyConfirmationCodes 
    {
        if (pendingAccount == null) {
            throw new NoPendingAccountConfirmationException();
        }
        else if (!pendingAccount.getConfirmationCode().equals(confirmationCode) && !pendingAccount.hasTooManyErrors()) {
            pendingAccountsCacheService.delete(pendingAccount.getEmail());
            pendingAccount.incrementErrorsCounter();
            pendingAccountsCacheService.store(pendingAccount, 10);
            throw new WrongConfirmationCodeException();
        }
        else if (pendingAccount.hasTooManyErrors()) {
            pendingAccountsCacheService.delete(pendingAccount.getEmail());
            throw new TooManyConfirmationCodes();
        }
    }
}