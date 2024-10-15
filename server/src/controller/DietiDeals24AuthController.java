package controller;

import entity.Account;
import request.AccountRegistrationRequest;
import request.LoginRequest;
import request.RegistrationConfirmationRequest;
import response.AccountMinimalInformations;
import service.*;
import utils.PendingAccountRegistration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import authentication.JwtTokenAwareHttpHeaders;
import authentication.JwtTokenManager;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccessDeniedWrongAccountProviderException;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountValidationException;
import exceptions.NoAccountWithSuchEmailException;
import exceptions.NoPendingAccountConfirmationException;
import exceptions.TooManyConfirmationCodes;
import exceptions.UnrecognizedCityException;
import exceptions.UnrecognizedCountryException;
import exceptions.WrongConfirmationCodeException;
import jakarta.mail.MessagingException;
import utils.RandomStringGenerator;

@Transactional
@RestController
public class DietiDeals24AuthController {

    private final AccountValidationService accountValidationService;
    private final AccountManagementService accountManagementService;
    private final EmailService emailService;
    private final PendingAccountRegistrationCacheService pendingAccountsCacheService;
    private final JwtTokenManager jwtTokenManager;
    private final RandomStringGenerator randomStringGenerator;
    
    @Autowired
    public DietiDeals24AuthController(
        EmailService emailService,
        AccountManagementService accountManagementService,
        AccountValidationService accountValidationService,
        PendingAccountRegistrationCacheService pendingAccountsCacheService,
        RandomStringGenerator randomStringGenerationService,
        JwtTokenManager jwtTokenProvider
    ) {
        this.emailService = emailService;
        this.accountManagementService = accountManagementService;
        this.accountValidationService = accountValidationService;
        this.pendingAccountsCacheService = pendingAccountsCacheService;
        this.jwtTokenManager = jwtTokenProvider;
        this.randomStringGenerator = randomStringGenerationService;
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<AccountMinimalInformations> login(@RequestBody @NotNull LoginRequest request) 
        throws 
            NoAccountWithSuchEmailException,
            AccessDeniedBadCredentialsException, 
            AccessDeniedWrongAccountProviderException, 
            AccountValidationException
    {
        accountValidationService.validatePassword(request.getPassword());
        Account account = accountManagementService.performAccountLogin(request.getEmail(), request.getPassword());
        AccountMinimalInformations accountView = new AccountMinimalInformations(account);
        String jwtToken = jwtTokenManager.generateToken(account.getId());
        HttpHeaders headers = new JwtTokenAwareHttpHeaders(jwtToken);
        return ResponseEntity.ok().headers(headers).body(accountView);
    }

    @PostMapping(value = "/register/init", produces = "text/plain")
    public ResponseEntity<String> initializeRegistration(
        @RequestBody AccountRegistrationRequest accountRegistrationRequest
    ) 
        throws 
            AccountValidationException, 
            AccountAlreadyExistsException,
            UnrecognizedCityException, 
            UnrecognizedCountryException, 
            MessagingException 
    {
        accountValidationService.validateAccountRegistrationRequest(accountRegistrationRequest);
        String confirmationCode = randomStringGenerator.generateRandomNumericalString(5);
        PendingAccountRegistration registrationData = new PendingAccountRegistration(accountRegistrationRequest, confirmationCode);
        pendingAccountsCacheService.store(registrationData,10);
        emailService.sendRegistrationConfirmEmail(accountRegistrationRequest.getEmail(), accountRegistrationRequest.getUsername(), confirmationCode);
        return ResponseEntity.ok().body("an email was sent to: " + accountRegistrationRequest.getEmail());
    }

    @PostMapping(value = "/register/confirm", produces = "application/json")
    public ResponseEntity<AccountMinimalInformations> confirmRegistration(@RequestBody @NotNull RegistrationConfirmationRequest request) 
        throws 
            NoPendingAccountConfirmationException, 
            WrongConfirmationCodeException, 
            TooManyConfirmationCodes {
        PendingAccountRegistration pendingAccount = pendingAccountsCacheService.retrieve(request.getEmail());
        pendingAccountsCacheService.ensureValidConfirmationCode(pendingAccount, request.getCode());
        Account account = accountManagementService.createAccount(pendingAccount);
        pendingAccountsCacheService.delete(request.getEmail());
        AccountMinimalInformations accountView = new AccountMinimalInformations(account);
        String jwtToken = jwtTokenManager.generateToken(account.getId());
        HttpHeaders headers = new JwtTokenAwareHttpHeaders(jwtToken);
        return ResponseEntity.ok().headers(headers).body(accountView);
    }
}