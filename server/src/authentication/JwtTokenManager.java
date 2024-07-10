package authentication;

import java.time.Instant;
import java.util.Date;
import java.util.Random;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;


@Component
public class JwtTokenManager {

    private static final String secretHS512Key = generateSecretKey();

    public static String generateSecretKey() {
        int keyLength = 10;
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(keyLength);
        for (int i = 0; i < keyLength; i++) {
            int randomIndex = random.nextInt(characterSet.length());
            randomString.append(characterSet.charAt(randomIndex));
        }
        return randomString.toString();
    }

    public String generateToken(String email) {
        Date now = Date.from(Instant.now());
        Date expiryDate = new Date(now.getTime() + 864000000); //10 days
        return Jwts.builder().setSubject(email).setIssuedAt(now)
            .setExpiration(expiryDate).signWith(SignatureAlgorithm.HS512, secretHS512Key).compact();
    }

    public String getEmailFromJWT(String token) {
        return Jwts.parser()
            .setSigningKey(secretHS512Key)
            .parseClaimsJws(token)
            .getBody().getSubject();
    }

    public long getIssuedAt(String token) {
        return Jwts.parser()
            .setSigningKey(secretHS512Key)
            .parseClaimsJws(token)
            .getBody().getIssuedAt().getTime();
    }

    public long getExpiration(String token) {
        return Jwts.parser()
            .setSigningKey(secretHS512Key)
            .parseClaimsJws(token)
            .getBody().getExpiration().getTime();
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretHS512Key).parseClaimsJws(authToken);
            return true;
        } catch (Exception ex) {
            System.out.println("Invalid token: " + ex.getMessage());
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