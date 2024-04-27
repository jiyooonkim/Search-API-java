package org.analyzer;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.RestHighLevelClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
/*
View Controllers
    - URL과 뷰 이름을 매핑,
    - URL에 파라미터를 전달하거나 리다이렉트
*/


@RestController
public class AnalyzeController {

    @Value(value = "${elasticsearch.host}")
    private String host;
    @Value(value = "${elasticsearch.port}")
    private int port;
    @Value(value = "${elasticsearch.schema}")
    private String schema;
    @Value(value = "${elasticsearch.apiKey}")
    private String apiKey;



//    @GetMapping( "/sample_client")
//    public RestHighLevelClient sample_client() throws ElasticsearchException, IOException {
//        System.out.println("analyzeConfig : " + analyzeConfig);
//        return analyzeConfig.getRestHighLevelClients(host, port, schema, apiKey) ;
//    }

    @GetMapping( "/sample_client")
    public String sample_client() {
        System.out.println("analyzeConfig : " );
        return "eeee" ;
    }

    AnalyzeConfig analyzeConfig = new AnalyzeConfig();
    @GetMapping( "/sampleclient")
    public RestHighLevelClient getRestHighLevelClients() throws IOException {
        System.out.println("analyzeConfig : " + host+ port+ schema);
        return analyzeConfig.getRestHighLevelClients(host,  port,  schema,  apiKey) ;
    }


    @GetMapping("/sample_analyze")
    @ResponseBody
    public ResponseEntity<String> sample_analyze(@RequestParam(name = "qry") String qry) throws IOException, ElasticsearchException, JSONException {
        return analyzeConfig.getAnalyzeRequest(qry);
    }

}
