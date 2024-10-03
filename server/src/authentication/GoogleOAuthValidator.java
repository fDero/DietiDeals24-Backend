package authentication;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

import exceptions.AccessDeniedBadCredentialsException;

public class GoogleOAuthValidator extends GoogleIdTokenVerifier {
    
    public GoogleOAuthValidator(String clientId) {
        super(
            new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), 
                GsonFactory.getDefaultInstance()
            )
            .setAudience(Collections.singletonList(clientId))
        );
    }

    public GoogleIdToken allInOneverify(String idTokenString) 
        throws 
            AccessDeniedBadCredentialsException
    {
        GoogleIdToken idToken = null;
        if (idTokenString == null) {
            throw new AccessDeniedBadCredentialsException();
        }
        try {
            idToken = super.verify(idTokenString);
        } catch (GeneralSecurityException | IOException e) {
            throw new AccessDeniedBadCredentialsException();
        }
        if (idToken == null) {
            throw new AccessDeniedBadCredentialsException();
        }
        return idToken;
    }
}
