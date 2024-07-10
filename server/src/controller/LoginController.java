package controller;

import entity.Account;
import entity.Password;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccountValidationException;
import exceptions.NoAccountWithSuchEmailException;
import repository.AccountRepository;
import repository.PasswordRepository;
import request.LoginRequest;
import response.MinimalAccountInformations;
import service.AccountValidationService;
import service.EncryptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import authentication.JwtTokenProvider;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class LoginController {

    private final AccountValidationService accountValidationService;
    private final AccountRepository accountRepository;
    private final EncryptionService encryptionService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordRepository passwordRepository;

    @Autowired
    public LoginController(
        AccountValidationService accountValidationService,
        AccountRepository accountRepository,
        EncryptionService encryptionService,
        JwtTokenProvider jwtTokenProvider,
        PasswordRepository passwordRepository
    ){
        this.accountValidationService = accountValidationService;
        this.accountRepository = accountRepository;
        this.encryptionService = encryptionService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordRepository = passwordRepository;
    }

    @PostMapping(value = "/login", produces = "application/json")
    public ResponseEntity<MinimalAccountInformations> login(@RequestBody @NotNull LoginRequest request) 
        throws 
            AccountValidationException,
            NoAccountWithSuchEmailException,
            AccessDeniedBadCredentialsException
    {
        accountValidationService.validatePassword(request.getPassword());
        Account account = accountRepository.findAccountByEmail(request.getEmail())
            .orElseThrow(() -> new NoAccountWithSuchEmailException());
        Password password = passwordRepository.findPasswordByAccountId(account.getId())
            .orElseThrow(() -> new NoAccountWithSuchEmailException());
        String realPasswordSalt = password.getPasswordSalt();
        String realPasswordHash = password.getPasswordHash();
        String candidatePlainTextPassword = request.getPassword();
        String candidatePasswordHash = encryptionService.encryptPassword(candidatePlainTextPassword, realPasswordSalt);
        if (!candidatePasswordHash.equals(realPasswordHash)){
            throw new AccessDeniedBadCredentialsException();
        }
        account.setLastLogin(new Timestamp(System.currentTimeMillis()));
        accountRepository.save(account);
        MinimalAccountInformations accountView = new MinimalAccountInformations(account);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", jwtTokenProvider.generateToken(account.getEmail()));
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("X-Auth-Token");
        headers.setAccessControlAllowHeaders(allowedHeaders);
        return ResponseEntity.ok().headers(headers).body(accountView);
    }
}
