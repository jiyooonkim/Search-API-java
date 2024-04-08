package org.example;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@Getter
@Setter
@ToString
public class ElasticsearchProperty {

    private String host;
    private int port;
    private String apiKey;
    private String schema;

}
