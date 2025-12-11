package com.example.demo.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchConfig.class);

    @Value("${es.host}")
    private String esHost;

    @Value("${es.port}")
    private int esPort;

    @Bean
    public RestClient restClient() {
        logger.info("Initializing Elasticsearch connection to {}:{}", esHost, esPort);
        
        RestClientBuilder builder = RestClient.builder(new HttpHost(esHost, esPort))
                .setRequestConfigCallback(requestConfigBuilder -> requestConfigBuilder
                        .setConnectTimeout(5000)
                        .setSocketTimeout(60000))
                .setHttpClientConfigCallback(httpClientBuilder -> httpClientBuilder
                        .setMaxConnTotal(100)
                        .setMaxConnPerRoute(20));

        return builder.build();
    }

    @Bean
    public ElasticsearchTransport transport(RestClient restClient) {
        return new RestClientTransport(restClient, new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient elasticsearchClient(ElasticsearchTransport transport) {
        return new ElasticsearchClient(transport);
    }
}