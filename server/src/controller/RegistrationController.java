package controller;

import entity.Account;
import repository.AccountRepository;
import request.AccountRegistrationRequest;
import request.RegistrationConfirmationRequest;
import service.AccountValidationService;
import service.CacheManagementService;
import service.EmailService;
import service.EncryptionService;
import utils.PendingAccountRegistration;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Random;

@RestController
public class RegistrationController {

    private final AccountValidationService account_validation_service;
    private final AccountRepository account_repository;
    private final EmailService email_service;
    private final CacheManagementService cache_management_service;
    private final EncryptionService encryption_service;

    @Autowired
    public RegistrationController(
            EmailService email_service,
            AccountValidationService account_validation_service,
            AccountRepository account_repository,
            CacheManagementService cache_management_service,
            EncryptionService encryption_service
    ) {
        this.email_service = email_service;
        this.account_validation_service = account_validation_service;
        this.account_repository = account_repository;
        this.cache_management_service = cache_management_service;
        this.encryption_service = encryption_service;
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
            account_validation_service.validateAccountRegistrationRequest(request);
            String confirmation_code = this.generateConfirmationCode();
            PendingAccountRegistration registration_data = new PendingAccountRegistration(request, confirmation_code);
            cache_management_service.store(request.getEmail(),registration_data,10);
            email_service.sendRegistrationConfirmEmail(request.getEmail(), confirmation_code);
            return ResponseEntity.ok().body("an email was sent to: " + request.getEmail());
        }
        catch (IllegalArgumentException error_initializing_registration){
            return ResponseEntity.badRequest().body(error_initializing_registration.getMessage());
        }
    }

    @PostMapping("/register/confirm")
    public ResponseEntity<String> confirmRegistration(@RequestBody @NotNull RegistrationConfirmationRequest request) {
        PendingAccountRegistration pending_account = cache_management_service.retrieve(request.getEmail());
        if (pending_account == null) return ResponseEntity.badRequest().body("there is no account waiting for registration using this email right now");
        if (!pending_account.getConfirmation_code().equals(request.getConfirmation_code())) {
            return this.handleBadConfirmationAttempt(pending_account, request);
        }
        else {
            String passwordSalt = encryption_service.generateRandomSalt();
            String passwordHash = encryption_service.encryptPassword(pending_account.getPassword(), passwordSalt);
            Account account = new Account(pending_account, passwordHash, passwordSalt);
            account_repository.save(account);
            cache_management_service.delete(request.getEmail());
            return ResponseEntity.ok().body("your registration has been accepted, you're account is now activated");
        }
    }

    @NotNull
    private ResponseEntity<String> handleBadConfirmationAttempt(@NotNull PendingAccountRegistration pending_account, @NotNull RegistrationConfirmationRequest request) {
        if (pending_account.getErrors_counter().equals(2)) {
            cache_management_service.delete(request.getEmail());
            String error_message = "you inserted the wrong code three times, your registration has been rejected";
            return ResponseEntity.badRequest().body(error_message);
        }
        else {
            pending_account.incrementErrorsCounter();
            cache_management_service.store(request.getEmail(),pending_account,10);
            String warning_message = "you inserted the wrong code, remember, if you do it three times, your registration will be rejected";
            return ResponseEntity.badRequest().body(warning_message);
        }
    }
}