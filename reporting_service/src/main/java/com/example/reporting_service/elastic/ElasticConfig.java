package com.example.reporting_service.elastic;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.example.reporting_service.repo")
@ComponentScan(basePackages = {"com.example.reporting_service"})
public class ElasticConfig extends AbstractElasticsearchConfiguration
{

    @Value("${spring.elasticsearch.uris}")
    public String elasticHost;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient()
    {
        final ClientConfiguration config = ClientConfiguration.builder()
                .connectedTo(elasticHost)
                .build();

        return RestClients.create(config).rest();
    }
}
