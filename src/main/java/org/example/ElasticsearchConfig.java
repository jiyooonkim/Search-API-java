package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.indices.analyze.AnalyzeToken;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;

import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.AnalyzeRequest;
import org.elasticsearch.client.indices.AnalyzeResponse;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
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
public class ElasticsearchConfig {
//    ElasticsearchProperty prop = new ElasticsearchProperty();


//    @Value("${elasticsearch.host}")
//    private String host;
//
//    @Value("${elasticsearch.port}")
//    private String port;
//
//
//    @Value("${elasticsearch.serverURL}")
//    private String serverURL;
//
//    @Value("${elasticsearch.apiKey}")
//    private String apiKey;


    @Bean
    RestClientTransport esConnection1(
            RestClient restClient,
            ObjectProvider<RestClientOptions> restClientOptions) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper(), restClientOptions.getIfAvailable());
    }

    @Bean
    public String esConnection2() throws IOException{

        BasicCredentialsProvider credsProv = new BasicCredentialsProvider();
        credsProv.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("elastic", "elastic"));
        RestClient restClient = RestClient
                .builder(new HttpHost("localhost", 9200, "http"))
                .setHttpClientConfigCallback(hc -> hc
                        .setDefaultCredentialsProvider(credsProv)
                )
                .build();
        return restClient.toString();
//        return RestClients.create(clientConfiguration).rest();

    }

    @Bean
    public ElasticsearchClient apiEsConnection() throws  IOException{

        String serverURL = "http://localhost:9200";
        String apiKey = "RVVzYktJNEI2M2RCTGNORUlOeGM6SnFSTTgwV3RRN21ZLTBrN3dHQ1J0dw==";
        RestClient restClient = RestClient.builder(
                HttpHost.create(serverURL)).setDefaultHeaders(
                new Header[]{
                        new BasicHeader("Authorization", "ApiKey " + apiKey)
                }
        ).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
        );
        ElasticsearchClient esClient = new co.elastic.clients.elasticsearch.ElasticsearchClient(transport);
        return esClient;

    }

    @Bean
    public GetResponse<Product> getIndexExample() throws IOException{
        String serverURL = "http://localhost:9200";
        String apiKey = "RVVzYktJNEI2M2RCTGNORUlOeGM6SnFSTTgwV3RRN21ZLTBrN3dHQ1J0dw==";
        RestClient restClient = RestClient.builder(
                HttpHost.create(serverURL)).setDefaultHeaders(
                        new Header[]{
                            new BasicHeader("Authorization", "ApiKey " + apiKey)
                        }
                ).build();

        ElasticsearchTransport transport = new RestClientTransport(
                restClient,
                new JacksonJsonpMapper()
            );
        ElasticsearchClient esClient = new co.elastic.clients.elasticsearch.ElasticsearchClient(transport);

        GetResponse<Product> response = esClient
            .get(
                g->g
                .index("my_index")
                .id("1"),
                Product.class
            );

        if (response.found()){
            Product product = response.source();
            log.info("Name : {}        Message : {}", product.getName(), product.getMessage());
        } else {
            System.out.println();
            log.error("Not found Index!!!");
        }
        return response;
    }



//    @Bean
//    public RestHighLevelClient getRestHighLevelClient() throws IOException {
//        String apiKey = prop.getApiKey();
//        System.out.println("getRestHighLevelClient apiKey : " + apiKey);
//        Header[] headers = new Header[]{
//                new BasicHeader("Authorization", "ApiKey " + apiKey)
//        };
//
//        RestHighLevelClient client = new RestHighLevelClient(
//                RestClient.builder(
//                                new HttpHost("localhost", 9200, "http"))
//                        .setHttpClientConfigCallback(
//                                httpClientBuilder -> httpClientBuilder.setDefaultHeaders(
//                                        Arrays.asList(headers)
//                                )
//                        )
//        );
//        return  client;
//    }


//    Create Index
    @Bean
    public String createIndexRequest() {
//        Todo : Create Index
        return  "Create Index";
    }

    // Delete Index
    @Bean
    public String DeleteIndexRequest() {
        //        Todo : Delete Index
        org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest request = new DeleteIndexRequest("my_index");
//        AcknowledgedResponse deleteIndexResponse = esclient.indices().delete(request, requestOptions);
//        boolean acknowledged = deleteIndexResponse.
        return  "Delete Index ";
    }


}
