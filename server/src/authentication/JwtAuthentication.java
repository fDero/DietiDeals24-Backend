package authentication;

import java.util.Collection;
import java.util.Collections;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import jakarta.servlet.http.HttpServletRequest;


public class JwtAuthentication implements Authentication {

    private final String token;
    private final String id;
    private final HttpServletRequest request;
    
    JwtAuthentication(String token, String id, HttpServletRequest request){
        this.token = token;
        this.id = id;
        this.request = request;
    }

    @Override
    public String getName() {
       return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return request;
    }

    @Override
    public Object getPrincipal() {
        return id;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException { }
    
}
