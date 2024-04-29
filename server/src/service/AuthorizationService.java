package service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import entity.Account;
import java.sql.Date;
import java.util.Random;

import org.springframework.stereotype.Service;


@Service 
public class AuthorizationService {
    
    private final String secretHMAC256key;
    private final Algorithm algorithm;;

    public AuthorizationService() {
        int stringLength = 10;
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(stringLength);
        for (int i = 0; i < stringLength; i++) {
            int randomIndex = random.nextInt(characterSet.length());
            randomString.append(characterSet.charAt(randomIndex));
        }
        secretHMAC256key = randomString.toString();
        algorithm = Algorithm.HMAC256(secretHMAC256key);
    }
    
    private Date getExpirationTime(int days, int hours, int minutes){
        long millisecondsInADay = 86400000;
        long millisecondsInAnHour = 3600000;
        long millisecondsInAMinute = 60000;
        return new Date(
            System.currentTimeMillis() + 
            days * millisecondsInADay + 
            hours * millisecondsInAnHour + 
            minutes * millisecondsInAMinute
        );
    }

    public String emitAuthorizationToken(Account account){
        String token = JWT.create()
            .withIssuer("dietideals24")
            .withExpiresAt(getExpirationTime(0,1,0))
            .withSubject(account.getEmail())
            .withAudience("User")
            .withIssuedAt(new Date(System.currentTimeMillis()))
            .sign(algorithm);
        return token;
    }

    public DecodedJWT decodeJwtToken(String token, Account account){
        return JWT.require(algorithm)
            .withIssuer("dietideals24")
            .build()
            .verify(token);        
    }

    public String replaceAuthorizationTokenIfNeeded(String token, Account account){
        DecodedJWT decodedToken = decodeJwtToken(token, account);
        long timeLeft = (decodedToken.getExpiresAt().getTime() - System.currentTimeMillis());
        long originalTimeSpan = (decodedToken.getExpiresAt().getTime() - decodedToken.getIssuedAt().getTime());
        boolean isAboutToExpire = timeLeft < originalTimeSpan / 2;
        return isAboutToExpire ? emitAuthorizationToken(account) : token;
    }
}
