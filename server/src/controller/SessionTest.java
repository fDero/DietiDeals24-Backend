package controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SessionTest {

    @GetMapping("/sessiontest/print")
    public ResponseEntity<String> test(HttpServletRequest request){
        String COUNTER = "COUNTER";
        Integer counter = (Integer) request.getSession().getAttribute(COUNTER);
        if (counter == null) counter = 0;
        else counter++;
        request.getSession().setAttribute(COUNTER, counter);
        return ResponseEntity.ok().body(counter.toString());
    }
}
