package authentication;

import java.time.Instant;
import java.util.Date;
import java.util.Random;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

import org.springframework.stereotype.Component;


@Component
public class JwtTokenProvider {

    private final String secretHS512Key;

    public JwtTokenProvider() {
        int keyLength = 10;
        String characterSet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder randomString = new StringBuilder(keyLength);
        for (int i = 0; i < keyLength; i++) {
            int randomIndex = random.nextInt(characterSet.length());
            randomString.append(characterSet.charAt(randomIndex));
        }
        secretHS512Key = randomString.toString();
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

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(secretHS512Key).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
            System.out.println("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            System.out.println("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            System.out.println("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            System.out.println("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            System.out.println("JWT claims string is empty.");
        }
        return false;
    }
}