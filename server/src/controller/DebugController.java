package controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpServletRequest;

@RestController
public class DebugController {

    class SanityCheckResponse {
        public String serverStatus = "Server is running";
    }

    @GetMapping("/debug/sanity-check")
    @PostMapping("/debug/sanity-check")
    public ResponseEntity<SanityCheckResponse> sanityCheck(HttpServletRequest request) {
        return ResponseEntity.ok().body(new SanityCheckResponse());
    }

    class ErrorReportExample {
        public String error = "This is an example error message";
    }
    
    @GetMapping("/debug/bad-request-demo")
    @PostMapping("/debug/bad-request-demo")
    public ResponseEntity<ErrorReportExample> badRequestDemo(HttpServletRequest request) {
        return ResponseEntity.badRequest().body(new ErrorReportExample());
    }

    public class ExceptionExample extends RuntimeException {
        public String error = "This is an example error message";
    }
    
    @GetMapping("/debug/exception-intercept-demo")
    @PostMapping("/debug/exception-intercept-demo")
    public ResponseEntity<SanityCheckResponse> exceptionInterceptionDemo(HttpServletRequest request) {
        throw new ExceptionExample();
    }
}
