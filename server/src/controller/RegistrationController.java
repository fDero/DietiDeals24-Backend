package controller;

import entity.Account;
import entity.Password;
import repository.AccountRepository;
import repository.PasswordRepository;
import request.AccountRegistrationRequest;
import request.RegistrationConfirmationRequest;
import response.MinimalAccountInformations;
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

import authentication.JwtTokenProvider;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountValidationException;
import exceptions.NoPendingAccountConfirmationException;
import exceptions.TooManyConfirmationCodes;
import exceptions.WrongConfirmationCodeException;

@Transactional
@RestController
public class RegistrationController {

    private final AccountValidationService accountValidationService;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final PendingAccountRegistrationCacheService pendingAccountsCacheService;
    private final EncryptionService encryptionService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordRepository passwordRepository;

    @Autowired
    public RegistrationController(
            EmailService emailService,
            AccountValidationService accountValidationService,
            AccountRepository accountRepository,
            PendingAccountRegistrationCacheService pendingAccountsCacheService,
            EncryptionService encryptionService,
            JwtTokenProvider jwtTokenProvider,
            PasswordRepository passwordRepository
    ) {
        this.emailService = emailService;
        this.accountValidationService = accountValidationService;
        this.accountRepository = accountRepository;
        this.pendingAccountsCacheService = pendingAccountsCacheService;
        this.encryptionService = encryptionService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordRepository = passwordRepository;
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
            AccountAlreadyExistsException 
    {
        accountValidationService.validateAccountRegistrationRequest(request);
        String confirmationCode = this.generateConfirmationCode();
        PendingAccountRegistration registrationData = new PendingAccountRegistration(request, confirmationCode);
        pendingAccountsCacheService.store(registrationData,10);
        emailService.sendRegistrationConfirmEmail(request.getEmail(), confirmationCode);
        return ResponseEntity.ok().body("an email was sent to: " + request.getEmail());
    }

    @PostMapping(value = "/register/confirm", produces = "application/json")
    public ResponseEntity<MinimalAccountInformations> confirmRegistration(@RequestBody @NotNull RegistrationConfirmationRequest request) 
        throws 
            NoPendingAccountConfirmationException, 
            WrongConfirmationCodeException, 
            TooManyConfirmationCodes 
    {
        PendingAccountRegistration pendingAccount = pendingAccountsCacheService.retrieve(request.getEmail());
        ensureValidConfirmationCode(pendingAccount, request.getCode());
        String passwordSalt = encryptionService.generateRandomSalt();
        String passwordHash = encryptionService.encryptPassword(pendingAccount.getPassword(), passwordSalt);
        Account account = accountRepository.save(new Account(pendingAccount));
        Password password = new Password(passwordSalt, passwordHash, account.getId());
        passwordRepository.save(password);
        pendingAccountsCacheService.delete(request.getEmail());
        MinimalAccountInformations accountView = new MinimalAccountInformations(account);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", jwtTokenProvider.generateToken(account.getEmail()));
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("X-Auth-Token");
        headers.setAccessControlAllowHeaders(allowedHeaders);
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