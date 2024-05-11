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

    private JwtTokenProvider tokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull  HttpServletResponse response,@NonNull  FilterChain filterChain)
            throws 
                ServletException, 
                IOException 
    {
        System.out.println("Filtering requests");
        
        String token = tokenProvider.getTokenFromRequest(request);
        System.out.println("token: " + token);
        
        
        if (token != null && tokenProvider.validateToken(token)) {

            System.out.println("Token is valid");
            String email = tokenProvider.getEmailFromJWT(token);
            Authentication auth = new JWTAuthentication(token, email, request);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}