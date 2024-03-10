package org.example;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import org.apache.http.HttpHost;

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
        System.out.println(elasticsearchConfig.elasticsearchTest());
        return elasticsearchConfig.elasticsearchTest().toString();
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
