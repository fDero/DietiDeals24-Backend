package authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenManager tokenProvider;

    public JwtAuthenticationFilter(JwtTokenManager tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request, 
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    )
        throws 
            ServletException,
            IOException 
    {
        String token = tokenProvider.getTokenFromRequest(request);
        boolean tokenIsValid = token != null && tokenProvider.validateToken(token);
        System.out.println("Token is valid: " + tokenIsValid);

        if (!tokenIsValid) {
            filterChain.doFilter(request, response);
            return;
        }

        String id = tokenProvider.getIdFromJWT(token);
        Authentication auth = new JwtAuthentication(token, id, request);
        SecurityContextHolder.getContext().setAuthentication(auth);

        long tokenUpTime = System.currentTimeMillis() - tokenProvider.getIssuedAt(token);
        long tokenFullLifeSpan = tokenProvider.getExpiration(token) - tokenProvider.getIssuedAt(token);
        String tokenWasRenewed = "No";

        if (tokenUpTime > tokenFullLifeSpan / 2) {
            token = tokenProvider.generateToken(id);
            tokenWasRenewed = "Yes";
        }

        response.addHeader("X-Auth-Token", token);
        response.addHeader("X-Token-Was-Renewed", tokenWasRenewed);
        response.addHeader("Access-Control-Expose-Headers", "X-Auth-Token, X-Token-Was-Renewed");
        filterChain.doFilter(request, response);
    }
}