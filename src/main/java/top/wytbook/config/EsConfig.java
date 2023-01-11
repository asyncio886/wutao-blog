package top.wytbook.config;

import lombok.Data;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "es")
@Data
public class EsConfig {
    String url;
    @Bean
    public RestHighLevelClient restClient() {
        RestClientBuilder builder = RestClient.builder(HttpHost.create(url));
        return new RestHighLevelClient(builder);
    }
}
