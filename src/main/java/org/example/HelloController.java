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

    @GetMapping("/es")
    public String es() throws IOException {
        System.out.println(elasticsearchConfig.esConnection2());
        return elasticsearchConfig.esConnection2().toString();
    }

    @GetMapping("/es1")
    public GetResponse<Product> es1() throws IOException {
        return elasticsearchConfig.esConnection3();
    }

//    @GetMapping("/idx")
//    public String idx() throws IOException {
//        System.out.println(elasticsearchConfig.elasticsearchClient());
//        return elasticsearchConfig.elasticsearchClient().toString();
//    }

//    @GetMapping(value ="/item")
//    public ResponseEntity<String> search(@RequestParam(name = "id") String id) throws IOException{
//
//        IndexResponse response = esClient.index
//
//        return client;
//    }


}
