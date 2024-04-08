package org.analyzer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@ConfigurationProperties(prefix = "elasticsearch")
@Getter
@Setter
@ToString
public class AnalyzeProperty {
    private String host;
    private String apiKey;
    private String schema;
    private int port;
    private String serverURL;
}
