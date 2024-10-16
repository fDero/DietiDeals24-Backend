package authentication;

import java.time.Instant;
import java.util.Date;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import utils.RandomStringGenerator;


@Component
public class JwtTokenManager {

    private final String secretKey;

    @Autowired
    JwtTokenManager(RandomStringGenerator randomStringGenerationService){
        secretKey = randomStringGenerationService.generateRandomString(10);
    }

    public String generateToken(Integer id) {
        return generateToken(id.toString());
    }

    public String generateToken(String id) {
        Date now = Date.from(Instant.now());
        Date expiryDate = new Date(now.getTime() + 864000000); //10 days
        return Jwts.builder().setSubject(id).setIssuedAt(now)
            .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, secretKey).compact();
    }

    public String getIdFromJWT(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody().getSubject();
    }

    public long getIssuedAt(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody().getIssuedAt().getTime();
    }

    public long getExpiration(String token) {
        return Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(token)
            .getBody().getExpiration().getTime();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return getTokenFromRequestHeader(bearerToken);
    }

    public String getTokenFromRequestHeader(String header) {
        String bearerToken = header;
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}