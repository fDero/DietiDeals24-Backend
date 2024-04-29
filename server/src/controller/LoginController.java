package controller;

import entity.Account;
import repository.AccountRepository;
import request.LoginRequest;
import service.AccountValidationService;
import service.AuthorizationService;
import service.EncryptionService;
import service.JsonConversionService;
import utils.AccountWithoutSensibleInformations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
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
    private final JsonConversionService jsonConverter;
    private final AuthorizationService authorizationService;

    @Autowired
    public LoginController(
        AccountValidationService accountValidationService,
        AccountRepository accountRepository,
        EncryptionService encryptionService,
        JsonConversionService jsonConverter,
        AuthorizationService authorizationService
    ){
        this.jsonConverter = jsonConverter;
        this.accountValidationService = accountValidationService;
        this.accountRepository = accountRepository;
        this.encryptionService = encryptionService;
        this.authorizationService = authorizationService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @NotNull LoginRequest request) {
        if (!accountValidationService.isValidPassword(request.getPassword())) {
            return ResponseEntity.badRequest().body("access denied: ill-formed-password");
        }
        Optional<Account> retrieved = accountRepository.findAccountByEmail(request.getEmail());
        if (retrieved.isEmpty()) {
            return ResponseEntity.badRequest().body("there is no account with such email");
        }
        Account account = retrieved.get();
        String realPasswordSalt = account.getPasswordSalt();
        String realPasswordHash = account.getPasswordHash();
        String candidatePlainTextPassword = request.getPassword();
        String candidatePasswordHash = encryptionService.encryptPassword(candidatePlainTextPassword, realPasswordSalt);
        if (!candidatePasswordHash.equals(realPasswordHash)){
            return ResponseEntity.badRequest().body("access denied: bad credentials");
        }
        AccountWithoutSensibleInformations accountView = new AccountWithoutSensibleInformations(account);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Auth-Token", authorizationService.emitAuthorizationToken(account));
        ResponseEntity<String> response = ResponseEntity.ok().headers(headers).body(jsonConverter.encode(accountView));
        return response;
    }
}
