package controller;

import entity.Account;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccountValidationException;
import exceptions.NoAccountWithSuchEmailException;
import repository.AccountRepository;
import request.LoginRequest;
import service.AccountValidationService;
import service.AuthorizationService;
import service.EncryptionService;
import service.JsonConversionService;
import utils.MinimalAccountInformations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.sql.Timestamp;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    private final AccountValidationService accountValidationService;
    private final AccountRepository accountRepository;
    private final EncryptionService encryptionService;
    private final AuthorizationService authorizationService;

    @Autowired
    public LoginController(
        AccountValidationService accountValidationService,
        AccountRepository accountRepository,
        EncryptionService encryptionService,
        JsonConversionService jsonConverter,
        AuthorizationService authorizationService
    ){
        this.accountValidationService = accountValidationService;
        this.accountRepository = accountRepository;
        this.encryptionService = encryptionService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<MinimalAccountInformations> login(@RequestBody @NotNull LoginRequest request) 
        throws 
            AccountValidationException,
            NoAccountWithSuchEmailException,
            AccessDeniedBadCredentialsException
    {
        accountValidationService.validatePassword(request.getPassword());
        Optional<Account> retrieved = accountRepository.findAccountByEmail(request.getEmail());
        if (retrieved.isEmpty()) {
            throw new NoAccountWithSuchEmailException();
        }
        Account account = retrieved.get();
        String realPasswordSalt = account.getPasswordSalt();
        String realPasswordHash = account.getPasswordHash();
        String candidatePlainTextPassword = request.getPassword();
        String candidatePasswordHash = encryptionService.encryptPassword(candidatePlainTextPassword, realPasswordSalt);
        if (!candidatePasswordHash.equals(realPasswordHash)){
            throw new AccessDeniedBadCredentialsException();
        }
        account.setLastLogin(new Timestamp(System.currentTimeMillis()));
        accountRepository.save(account);
        MinimalAccountInformations accountView = new MinimalAccountInformations(account);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", authorizationService.emitAuthorizationToken(account));
        return ResponseEntity.ok().headers(headers).body(accountView);
    }
}
