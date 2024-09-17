package controller;

import entity.Account;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccountValidationException;
import exceptions.NoAccountWithSuchEmailException;
import exceptions.NoAccountWithSuchUsernameException;
import exceptions.NoPasswordForThisAccountException;
import request.ForgotPasswordInitializationRequest;
import request.ForgotPasswordResetRequest;
import service.AccountManagementService;
import service.EmailService;
import service.ForgotPasswordConfirmationCache;
import service.RandomStringGenerationService;
import utils.PendingForgotPasswordReset;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import authentication.JwtTokenManager;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class PasswordController {

    private final AccountManagementService accountManagementService;
    private final ForgotPasswordConfirmationCache forgotPasswordPendingConfirmationCache;
    private final EmailService emailService;
    private final String frontendUrl;
    private final RandomStringGenerationService randomStringGenerationService;

    @Autowired
    public PasswordController(
        @Value("${dietideals24.frontend.url}") String frontendUrl,
        RandomStringGenerationService randomStringGenerationService,
        AccountManagementService accountManagementService,
        JwtTokenManager jwtTokenProvider, 
        ForgotPasswordConfirmationCache forgotPasswordPendingConfirmationCache,
        EmailService emailService
    ){
        this.accountManagementService = accountManagementService;
        this.forgotPasswordPendingConfirmationCache = forgotPasswordPendingConfirmationCache;
        this.emailService = emailService;
        this.frontendUrl = frontendUrl;
        this.randomStringGenerationService = randomStringGenerationService;
    }

    @PostMapping(value = "/password/forgot/reset/init", produces = "text/plain")
    public ResponseEntity<String> initializeForgotPassword(
        @RequestBody ForgotPasswordInitializationRequest forgotPasswordInitializationRequest
    ) 
        throws 
            NoAccountWithSuchEmailException,
            NoAccountWithSuchUsernameException
    {
        final boolean haveUsername = forgotPasswordInitializationRequest.getUsername() != null;
        final boolean haveEmail = forgotPasswordInitializationRequest.getEmail() != null;
        if (!haveUsername && !haveEmail) {
            return ResponseEntity.badRequest().body("You must provide either an email or a username");
        }
        if (haveUsername && haveEmail) {
            return ResponseEntity.badRequest().body("You must provide either an email or a username, not both");
        }
        final Account account = haveUsername? 
            accountManagementService.fetchAccountByUsername(forgotPasswordInitializationRequest.getUsername()) :
            accountManagementService.fetchAccountByEmail(forgotPasswordInitializationRequest.getUsername()) ;
        String authToken = randomStringGenerationService.generateRandomString(10);
        PendingForgotPasswordReset pendingForgotPasswordReset = new PendingForgotPasswordReset(
            account.getEmail(),
            account.getUsername(),
            account.getId(),
            authToken
        );
        forgotPasswordPendingConfirmationCache.store(pendingForgotPasswordReset, 5);
        emailService.sendForgotPasswordEmail(account, frontendUrl + "/reset-password/" + account.getId() + "/" + authToken);
        return ResponseEntity.ok().body("an email was sennt to: " + account.getEmail());
    }

    @PostMapping(value = "/password/forgot/reset/finalize", produces = "text/plain")
    public ResponseEntity<String> resetForgotPassword(
        @RequestBody ForgotPasswordResetRequest forgotPasswordInitializationRequest
    ) 
        throws 
            NoAccountWithSuchEmailException,
            NoAccountWithSuchUsernameException, 
            NoPasswordForThisAccountException, 
            AccountValidationException, 
            AccessDeniedBadCredentialsException
    {
        final Integer accountId = forgotPasswordInitializationRequest.getUserId();
        final String authToken = forgotPasswordInitializationRequest.getAuthToken();
        final PendingForgotPasswordReset pendingForgotPasswordReset = forgotPasswordPendingConfirmationCache.retrieve(accountId);
        if (pendingForgotPasswordReset == null) {
            return ResponseEntity.badRequest().body("No pending reset password request for this email");
        }
        if (!pendingForgotPasswordReset.getAccountId().equals(accountId)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email");
        } 
        if (!pendingForgotPasswordReset.getAuthToken().equals(authToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid auth token");
        }
        accountManagementService.updatePassword(forgotPasswordInitializationRequest, accountId);
        return ResponseEntity.ok().body("done");
    }
}