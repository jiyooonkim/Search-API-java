package org.analyzer;

import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@RequiredArgsConstructor
public class AnalyzeConfig {
    @Bean
    RestClientTransport esConnection1(
            RestClient restClient,
            ObjectProvider<RestClientOptions> restClientOptions) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper(), restClientOptions.getIfAvailable());
    }
    /*
     *   Es setting 값 args 로 받는 방식
     */
    public RestHighLevelClient getRestHighLevelClients(String host, Integer port, String schema, String apiKey) throws ElasticsearchException {
        log.info("---[Client Info] apiKey {}     host {}     port: {}    schema : {}", apiKey ,  host , port, schema);
        Header[] headers = new Header[]{
                new BasicHeader("Authorization", "ApiKey " + apiKey)
            };
        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost(host, port, schema)
            ).setHttpClientConfigCallback(
                httpClientBuilder -> httpClientBuilder.setDefaultHeaders(
                    Arrays.asList(headers)
                )
            )
        );
        return client;
    }

    public RestHighLevelClient esConnection() throws ElasticsearchException {
        String apiKey = "RVVzYktJNEI2M2RCTGNORUlOeGM6SnFSTTgwV3RRN21ZLTBrN3dHQ1J0dw==";
        Header[] headers = new Header[]{
            new BasicHeader("Authorization", "ApiKey " + apiKey)
        };

        RestHighLevelClient client = new RestHighLevelClient(
            RestClient.builder(
                    new HttpHost("localhost", 9200, "http"))
                .setHttpClientConfigCallback(
                    httpClientBuilder -> httpClientBuilder.setDefaultHeaders(
                        Arrays.asList(headers)
                    )
                )
        );
        return client;
    }

    public  Boolean checkExistIndex(String indexName){
        /* Index 존재 여부 확인
            @return : True/False
        */
        Boolean ack;
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            ack = esConnection().indices().exists(getIndexRequest, RequestOptions.DEFAULT);
            log.info("--- [indexName, ack] {} , {}", indexName, ack);
            return ack;
        } catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    /*
    *  Get 방식 - qry 로 질의 input custom dictionary(동의어, 불용어) 뒤져서 결과 나옴
    */
    @ResponseBody
    public ResponseEntity<String> getStopwordAnalyzeRequest(String qry) throws ElasticsearchException, IOException, JSONException {
        AnalyzeRequest request = AnalyzeRequest
                .buildCustomAnalyzer("stopword_test", "whitespace")
                .addTokenFilter("stop_filter")
                .build(qry);

        AnalyzeResponse response = esConnection().indices()
                .analyze(request, RequestOptions.DEFAULT);

        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
        log.info("qry : {}", qry);

        JSONObject msg = new JSONObject();
        for(AnalyzeResponse.AnalyzeToken token : tokens){
            msg.append("terms : ", token.getTerm());
        }

        ActionListener<AnalyzeResponse> listener = new ActionListener<AnalyzeResponse>() {
            @Override
            public void onResponse(AnalyzeResponse analyzeTokens) {
                System.out.println(response.getTokens().toArray());
                System.out.println(analyzeTokens);
                log.info("INFO-- ****SUCCESS****");
            }
            @Override
            public void onFailure(Exception e) {
                log.error("Fail!!!!! Cause : " + e.getMessage());
            }
        };
        listener.onResponse(response);
        return new ResponseEntity<>(msg.toString(), HttpStatus.OK);
    }


    @ResponseBody
    public ResponseEntity<String> getSynonAnalyzeRequest(String qry) throws ElasticsearchException, IOException, JSONException {
        AnalyzeRequest request = AnalyzeRequest.withIndexAnalyzer(
                "synonym_test",
                "synonym_analyzer",
                qry
            );

        AnalyzeResponse response = esConnection().indices()
                .analyze(request, RequestOptions.DEFAULT);

        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
        log.info("qry : {}", qry);

        JSONObject msg = new JSONObject();
        for(AnalyzeResponse.AnalyzeToken token : tokens){
            msg.append("terms : ", token.getTerm());
        }

        ActionListener<AnalyzeResponse> listener = new ActionListener<AnalyzeResponse>() {
            @Override
            public void onResponse(AnalyzeResponse analyzeTokens) {
                System.out.println(response.getTokens().toArray());
                System.out.println(analyzeTokens);
                log.info("INFO-- ****SUCCESS****");
            }
            @Override
            public void onFailure(Exception e) {
                log.error("Fail!!!!! Cause : " + e.getMessage());
            }
        };
        listener.onResponse(response);
        return new ResponseEntity<>(msg.toString(), HttpStatus.OK);
    }
}