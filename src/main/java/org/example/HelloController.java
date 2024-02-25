package org.example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class HelloController {

    @GetMapping("/test")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @GetMapping("/es")
    public String es() {
        return "es!";
    }
}
