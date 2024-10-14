package controller;

import authentication.RequireJWT;
import entity.Account;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccessDeniedWrongAccountProviderException;
import exceptions.AccountValidationException;
import exceptions.NoAccountWithSuchEmailException;
import exceptions.NoAccountWithSuchUsernameException;
import exceptions.NoPasswordForThisAccountException;
import jakarta.mail.MessagingException;
import org.springframework.web.bind.annotation.RequestHeader;
import request.ForgotPasswordResetInitializationRequest;
import request.ForgotPasswordResetConfirmationRequest;
import request.PasswordChangeRequest;
import service.AccountManagementService;
import service.EmailService;
import service.ForgotPasswordConfirmationCache;
import utils.RandomStringGenerator;
import utils.PendingForgotPasswordReset;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import authentication.JwtTokenManager;
import java.io.UnsupportedEncodingException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Transactional
@RestController
public class PasswordController {

    private final AccountManagementService accountManagementService;
    private final ForgotPasswordConfirmationCache forgotPasswordPendingConfirmationCache;
    private final EmailService emailService;
    private final JwtTokenManager jwtTokenManager;
    private final RandomStringGenerator randomStringGenerationService;

    @Autowired
    public PasswordController(
        RandomStringGenerator randomStringGenerationService,
        AccountManagementService accountManagementService,
        ForgotPasswordConfirmationCache forgotPasswordPendingConfirmationCache,
        EmailService emailService,
        JwtTokenManager jwtTokenManager
        ){
        this.accountManagementService = accountManagementService;
        this.forgotPasswordPendingConfirmationCache = forgotPasswordPendingConfirmationCache;
        this.emailService = emailService;
        this.randomStringGenerationService = randomStringGenerationService;
        this.jwtTokenManager = jwtTokenManager;
    }

    @PostMapping(value = "/password/forgot/reset/init", produces = "text/plain")
    public ResponseEntity<String> initializeForgotPassword(
        @RequestBody ForgotPasswordResetInitializationRequest forgotPasswordInitializationRequest
    ) 
        throws 
            NoAccountWithSuchEmailException,
            NoAccountWithSuchUsernameException, 
            UnsupportedEncodingException, 
            MessagingException,
            AccessDeniedWrongAccountProviderException
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
            accountManagementService.fetchAccountByEmail(forgotPasswordInitializationRequest.getEmail());
        if (!account.getAccountProvider().equals("DIETIDEALS24")) {
            throw new AccessDeniedWrongAccountProviderException();
        }
        String authToken = randomStringGenerationService.generateRandomString(10);
        PendingForgotPasswordReset pendingForgotPasswordReset = new PendingForgotPasswordReset(
            account.getEmail(),
            account.getUsername(),
            account.getId(),
            authToken
        );
        forgotPasswordPendingConfirmationCache.store(pendingForgotPasswordReset, 5);
        emailService.sendForgotPasswordEmail(account, authToken);
        return ResponseEntity.ok().body("an email was sent to: " + account.getEmail());
    }

    @PostMapping(value = "/password/forgot/reset/finalize", produces = "text/plain")
    public ResponseEntity<String> resetForgotPassword(
        @RequestBody ForgotPasswordResetConfirmationRequest forgotPasswordInitializationRequest
    ) 
        throws 
            NoPasswordForThisAccountException, 
            AccountValidationException
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

    @RequireJWT
    @PostMapping(value = "/profile/update/password", produces = "text/plain")
    public ResponseEntity<String> updatePassword(
        @RequestHeader(name = "Authorization") String authorizationHeader,
        @RequestBody PasswordChangeRequest passwordChangeRequest
    )
        throws
            NoPasswordForThisAccountException,
            AccountValidationException,
            AccessDeniedBadCredentialsException
    {
        String jwtToken = jwtTokenManager.getTokenFromRequestHeader(authorizationHeader);
        String accountIdString = jwtTokenManager.getIdFromJWT(jwtToken);
        Integer accountId = Integer.valueOf(accountIdString);
        accountManagementService.updatePassword(passwordChangeRequest, accountId);
        return ResponseEntity.ok().body("done");
    }
}