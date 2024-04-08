package org.analyzer;


import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.springframework.beans.factory.ObjectProvider;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

// 역할 : 하나 이상의 bean 메소드 제공 및 처리 역할
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@RequiredArgsConstructor
public class AnalyzeConfig   {


    @Bean
    RestClientTransport esConnection1(
            RestClient restClient,
            ObjectProvider<RestClientOptions> restClientOptions) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper(), restClientOptions.getIfAvailable());
    }

    public RestHighLevelClient getRestHighLevelClients(String host, Integer port, String schema, String apiKey) throws ElasticsearchException, IOException {
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

    @Bean
    public AnalyzeResponse getAnalyzeRequest() throws ElasticsearchException, IOException {

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

        AnalyzeRequest request = AnalyzeRequest
                .buildCustomAnalyzer("standard")
                .addTokenFilter("lowercase")
                .build("동해물과 백두산이AER12AER34sdf@#$WE34 aeeR");
//        co.elastic.clients.elasticsearch.indices.AnalyzeResponse response  = esClient.indices().analyze();
//        List<AnalyzeToken> tokens = response.tokens();

//        AnalyzeRequest request = AnalyzeRequest.buildCustomNormalizer()
//                .addTokenFilter("lowercase")
//                .build("BaRthe lazy dogAERWER")
//                .explain(true);
        AnalyzeResponse response = client.indices()
                .analyze(request, RequestOptions.DEFAULT);

        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();

        for(AnalyzeResponse.AnalyzeToken token : tokens){
            System.out.println("Terms : " + token.getTerm());
        }

        ActionListener<AnalyzeResponse> listener = new ActionListener<AnalyzeResponse>() {
            @Override
            public void onResponse(AnalyzeResponse analyzeTokens) {
                System.out.println(response.getTokens().toArray());
                log.info("INFO-- ****SUCCESS****");
            }

            @Override
            public void onFailure(Exception e) {
                log.error("Fail!!!!! Cause : " + e.getMessage());
            }
        };
//        client.indices().createAsync(request);

        return response;

    }



}