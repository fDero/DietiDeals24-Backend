package controller;

import entity.Account;
import repository.AccountRepository;
import request.AccountRegistrationRequest;
import request.RegistrationConfirmationRequest;
import service.AccountValidationService;
import service.AuthorizationService;
import service.PendingAccountRegistrationCacheService;
import service.EmailService;
import service.EncryptionService;
import service.JsonConversionService;
import utils.AccountWithoutSensibleInformations;
import utils.PendingAccountRegistration;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import java.util.Random;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegistrationController {

    private final JsonConversionService jsonConverter;
    private final AccountValidationService accountValidationService;
    private final AccountRepository accountRepository;
    private final EmailService emailService;
    private final PendingAccountRegistrationCacheService pendingAccountsCacheService;
    private final EncryptionService encryptionService;
    private final AuthorizationService authorizationService;

    @Autowired
    public RegistrationController(
            JsonConversionService jsonConverter,
            EmailService emailService,
            AccountValidationService accountValidationService,
            AccountRepository accountRepository,
            PendingAccountRegistrationCacheService pendingAccountsCacheService,
            EncryptionService encryptionService,
            AuthorizationService authorizationService
    ) {
        this.jsonConverter = jsonConverter;
        this.emailService = emailService;
        this.accountValidationService = accountValidationService;
        this.accountRepository = accountRepository;
        this.pendingAccountsCacheService = pendingAccountsCacheService;
        this.encryptionService = encryptionService;
        this.authorizationService = authorizationService;
    }

    @NotNull
    private String generateConfirmationCode(){
        Random random = new Random();
        StringBuilder randomGeneratedCode = new StringBuilder();
        for (int i = 0; i < 5; i++)
            randomGeneratedCode.append(random.nextInt(10));
        return randomGeneratedCode.toString();
    }

    @PostMapping("/register/init")
    public ResponseEntity<String> initializeRegistration(@RequestBody @NotNull AccountRegistrationRequest request) {
        try {
            accountValidationService.validateAccountRegistrationRequest(request);
            String confirmationCode = this.generateConfirmationCode();
            PendingAccountRegistration registrationData = new PendingAccountRegistration(request, confirmationCode);
            pendingAccountsCacheService.store(registrationData,10);
            emailService.sendRegistrationConfirmEmail(request.getEmail(), confirmationCode);
            return ResponseEntity.ok().body("an email was sent to: " + request.getEmail());
        }
        catch (IllegalArgumentException error){
            return ResponseEntity.badRequest().body(error.getMessage());
        }
    }

    @PostMapping("/register/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestBody @NotNull RegistrationConfirmationRequest request) {
        PendingAccountRegistration pendingAccount = pendingAccountsCacheService.retrieve(request.getEmail());
        if (pendingAccount == null) {
            return ResponseEntity.badRequest().body("there is no account waiting for registration using this email right now");
        }
        else if (!pendingAccount.getConfirmationCode().equals(request.getCode())) {
            System.out.println(pendingAccount.getConfirmationCode() + " <-official  recieved-> " + request.getCode());
            return this.handleBadConfirmationAttempt(pendingAccount, request);
        }
        else {
            String passwordSalt = encryptionService.generateRandomSalt();
            String passwordHash = encryptionService.encryptPassword(pendingAccount.getPassword(), passwordSalt);
            Account account = new Account(pendingAccount, passwordHash, passwordSalt);
            accountRepository.save(account);
            pendingAccountsCacheService.delete(request.getEmail());
            AccountWithoutSensibleInformations accountView = new AccountWithoutSensibleInformations(account);
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Auth-Token", authorizationService.emitAuthorizationToken(account));
            ResponseEntity<String> response = ResponseEntity.ok().headers(headers).body(jsonConverter.encode(accountView));
            return response;
        }
    }

    @NotNull
    private ResponseEntity<String> handleBadConfirmationAttempt(@NotNull PendingAccountRegistration pendingAccount, @NotNull RegistrationConfirmationRequest request) {
        if (pendingAccount.hasTooManyErrors()) {
            pendingAccountsCacheService.delete(request.getEmail());
            String errorMessage = "you inserted the wrong code three times, your registration has been rejected";
            return ResponseEntity.badRequest().body(errorMessage);
        }
        else {
            pendingAccount.incrementErrorsCounter();
            pendingAccountsCacheService.store(pendingAccount,10);
            String warningMessage = "you inserted the wrong code, remember, if you do it three times, your registration will be rejected";
            return ResponseEntity.badRequest().body(warningMessage);
        }
    }
}