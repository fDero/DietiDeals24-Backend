package provider;

import entity.Account;
import repository.AccountRepository;
import request.LoginRequest;
import service.AccountValidationService;
import service.EncryptionService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Optional;

@RestController
public class LoginController {

    private final AccountValidationService account_validation_service;
    private final AccountRepository account_repository;
    private final EncryptionService encryption_service;

    @Autowired
    public LoginController(
        AccountValidationService account_validation_service,
        AccountRepository account_repository,
        EncryptionService encryption_service
    ){
        this.account_validation_service = account_validation_service;
        this.account_repository = account_repository;
        this.encryption_service = encryption_service;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @NotNull LoginRequest request) {
        if (!account_validation_service.isValidPassword(request.getPassword())) return ResponseEntity.badRequest().body("access denied: ill-formed-password");
        Optional<Account> retrieved = account_repository.findAccountByEmail(request.getEmail());
        if (retrieved.isEmpty()) return ResponseEntity.badRequest().body("there is no account with such email");
        String candidate_plain_password = request.getPassword();
        String candidate_encrypted_password = encryption_service.encryptPassword(candidate_plain_password);
        String password = retrieved.get().getPassword();
        if (!candidate_encrypted_password.equals(password)) return ResponseEntity.badRequest().body("access denied: bad credentials");
        return ResponseEntity.ok().body("access granted, you're now authenticated");
    }
}
