package authentication;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;

public class JwtTokenAwareHttpHeaders extends HttpHeaders {
    
    public JwtTokenAwareHttpHeaders(String jwtToken) {
        super.set("X-Auth-Token", jwtToken);
        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("X-Auth-Token");
        super.setAccessControlExposeHeaders(allowedHeaders);
    }
}
