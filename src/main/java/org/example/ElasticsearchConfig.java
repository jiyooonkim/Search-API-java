package org.example;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientOptions;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

// 역할 : 하나 이상의 bean 메소드 제공 및 처리 역할
@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String host;

    @Value("${elasticsearch.port}")
    private String port;

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
    public GetResponse<Product> esConnection3() throws  IOException{
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
                        .id("Jongmin Kim"),
                        Product.class
                );
        if (response.found()){
            Product product = response.source();
            System.out.println("product : " + product);
        } else {
            System.out.println("Not found Index!!!");
        }


        return response;
    }




}
