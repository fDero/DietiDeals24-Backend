package controller;

import exceptions.AccessDeniedBadCredentialsException;
import request.OAuthTokenWrapperRequest;
import response.OAuthDebugInformations;
import service.AccountManagementService;
import service.AccountValidationService;

import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import authentication.JwtTokenManager;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;

@Transactional
@RestController
public class GoogleOAuthController {

    private final AccountValidationService accountValidationService;
    private final AccountManagementService accountManagementService;
    private final JwtTokenManager jwtTokenProvider;
    private final GoogleIdTokenVerifier verifier;
    
    @Autowired
    public GoogleOAuthController(
        AccountValidationService accountValidationService,
        AccountManagementService accountManagementService,
        @Value("${google.clientId}") String clientId,
        JwtTokenManager jwtTokenProvider
    ){
        this.accountValidationService = accountValidationService;
        this.accountManagementService = accountManagementService;
        this.jwtTokenProvider = jwtTokenProvider;
    
        NetHttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
            
        verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
            .setAudience(Collections.singletonList(clientId))
            .build();
    
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
        System.out.println("Received token: " + oauthTokenWrapper.getOauthToken());
        GoogleIdToken idToken = verifier.verify(oauthTokenWrapper.getOauthToken());
        if (idToken == null) {
            throw new AccessDeniedBadCredentialsException();
        }
        GoogleIdToken.Payload payload = idToken.getPayload();
        String email = payload.getEmail();
        String googleId = payload.getSubject();
        OAuthDebugInformations debugInformations = new OAuthDebugInformations(email, googleId);
        return ResponseEntity.ok(debugInformations);
    }
}