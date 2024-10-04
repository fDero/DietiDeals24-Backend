package controller;

import entity.Account;
import request.OAuthAccountRegistrationRequest;
import request.OAuthTokenWrapperRequest;
import response.AccountMinimalInformations;
import response.OAuthDebugInformations;
import service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.security.GeneralSecurityException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;

import authentication.GoogleOAuthValidator;
import authentication.JwtTokenAwareHttpHeaders;
import authentication.JwtTokenManager;
import exceptions.AccessDeniedBadCredentialsException;
import exceptions.AccountAlreadyExistsException;
import exceptions.AccountValidationException;
import exceptions.NoPendingAccountConfirmationException;
import exceptions.TooManyConfirmationCodes;
import exceptions.UnrecognizedCityException;
import exceptions.UnrecognizedCountryException;
import exceptions.WrongConfirmationCodeException;

@Transactional
@RestController
public class GoogleOAuthController {

    private final AccountValidationService accountValidationService;
    private final AccountManagementService accountManagementService;
    private final JwtTokenManager jwtTokenProvider;
    private final GoogleOAuthValidator googleOAuthVerifier;
    
    @Autowired
    public GoogleOAuthController(
        AccountManagementService accountManagementService,
        AccountValidationService accountValidationService,
        @Value("${google.clientId}") String googleOAuthClientId,
        JwtTokenManager jwtTokenProvider
    ) {
        this.accountManagementService = accountManagementService;
        this.accountValidationService = accountValidationService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.googleOAuthVerifier = new GoogleOAuthValidator(googleOAuthClientId);
    }

        @PostMapping(value = "/oauth/google/debug", produces = "application/json")
    public ResponseEntity<OAuthDebugInformations> tokenHealthCheck(
        @RequestBody OAuthTokenWrapperRequest oauthTokenWrapper
    ) 
        throws 
            GeneralSecurityException, 
            IOException, 
            AccessDeniedBadCredentialsException 
    {
        GoogleIdToken idToken = googleOAuthVerifier.allInOneverify(oauthTokenWrapper.getOauthToken());
        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String googleId = payload.getSubject();
        OAuthDebugInformations debugInformations = new OAuthDebugInformations(email, googleId);
        return ResponseEntity.ok(debugInformations);
    }

    @PostMapping(value = "/oauth/google/login", produces = "application/json")
    public ResponseEntity<AccountMinimalInformations> loginWithGoogle(
        @RequestBody OAuthTokenWrapperRequest oauthTokenWrapper
    ) 
        throws 
            AccessDeniedBadCredentialsException 
    {
        GoogleIdToken idToken = googleOAuthVerifier.allInOneverify(oauthTokenWrapper.getOauthToken());
        Account account = accountManagementService.performGoogleLogin(idToken);
        AccountMinimalInformations accountView = new AccountMinimalInformations(account);
        String jwtToken = jwtTokenProvider.generateToken(account.getId());
        HttpHeaders headers = new JwtTokenAwareHttpHeaders(jwtToken);
        return ResponseEntity.ok().headers(headers).body(accountView);  
    }

    @PostMapping(value = "/oauth/google/register", produces = "application/json")
    public ResponseEntity<AccountMinimalInformations> registerWithGoogle(
        @RequestBody OAuthAccountRegistrationRequest accountRegistrationRequest
    ) 
        throws 
            NoPendingAccountConfirmationException, 
            WrongConfirmationCodeException, 
            TooManyConfirmationCodes, 
            AccessDeniedBadCredentialsException, 
            AccountValidationException, 
            AccountAlreadyExistsException, 
            UnrecognizedCityException, 
            UnrecognizedCountryException 
    {
        GoogleIdToken idToken = googleOAuthVerifier.allInOneverify(accountRegistrationRequest.getOauthToken());
        String email = idToken.getPayload().getEmail();
        accountValidationService.validateAccountRegistrationRequest(email, accountRegistrationRequest);
        Account account = accountManagementService.createAccountWithGoogle(idToken, accountRegistrationRequest);
        AccountMinimalInformations accountView = new AccountMinimalInformations(account);
        String jwtToken = jwtTokenProvider.generateToken(account.getId());
        HttpHeaders headers = new JwtTokenAwareHttpHeaders(jwtToken);
        return ResponseEntity.ok().headers(headers).body(accountView);
    }
}