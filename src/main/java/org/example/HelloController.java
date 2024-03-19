package org.example;
import co.elastic.clients.elasticsearch.core.GetResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class HelloController {
    ElasticsearchConfig elasticsearchConfig = new ElasticsearchConfig();

    @GetMapping("/test")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
