package org.example;

import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import org.json.JSONObject;
import org.json.JSONException;

@RestController
public class ElasticsearchController {
    ElasticsearchConfig elasticsearchConfig = new ElasticsearchConfig();

    @GetMapping("/es")
    public String es() throws IOException {
        System.out.println(elasticsearchConfig.esConnection2());
        return elasticsearchConfig.esConnection2().toString();
    }

    @GetMapping("/es1")
    public GetResponse<Product> es1() throws IOException {
        return elasticsearchConfig.getIndexExample();
    }


    @GetMapping(value = "/qry")
    public @ResponseBody ResponseEntity<String> search(@RequestParam(name = "id") String id) throws IOException, JSONException {
//        Usage : http://localhost:8087/qry?id=1
        String name = "";
        String message = "";
        String etc = "";
        SearchResponse<Product> search = elasticsearchConfig
                .apiEsConnection()
                .search(
                    s ->s
                        .index("my_index")
                        .query(
                            q ->q
                                .term(
                                    t->t
                                        .field("_id")
                                        .value(
                                            v->v.stringValue(id))
                                        )
                            ),
                    Product.class
                );

        for (Hit<Product> hit: search.hits().hits()){
            name = hit.source().getName();
            message = hit.source().getMessage();
            etc = hit.source().getEtc();
            System.out.println("name: " + name);
            System.out.println("message: " + message);
            System.out.println("etc: " + etc);

        }
        JSONObject msg = new JSONObject();
        msg.put("name : ", name);
        msg.put("message : ", message);
        msg.put("etc : ", etc);

        return new ResponseEntity<>(msg.toString(), HttpStatus.OK);
    }




}
