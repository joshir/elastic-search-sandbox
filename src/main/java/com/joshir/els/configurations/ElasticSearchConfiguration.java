package com.joshir.els.configurations;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.erhlc.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Configuration
@Slf4j
@EnableElasticsearchRepositories(basePackages = {"com.joshir.els"})
public class ElasticSearchConfiguration extends AbstractElasticsearchConfiguration {
  private final ElasticSearchProps elasticSearchProps;

  public ElasticSearchConfiguration(ElasticSearchProps elasticSearchProps) {
    this.elasticSearchProps = elasticSearchProps;
  }

  @PostConstruct
  public void init() {
    log.info("initialized rest high level client");
  }

  @Bean
  @Override
  public RestHighLevelClient elasticsearchClient() {
    UriComponents uri = UriComponentsBuilder.fromHttpUrl(elasticSearchProps.getConnUri()).build();

    RestClientBuilder.RequestConfigCallback  reqConfigCallback =
      reqConfigBuilder -> reqConfigBuilder
        .setConnectTimeout(elasticSearchProps.getConnTimeOutMs())
        .setSocketTimeout(elasticSearchProps.getSocketTimeOutMs());

    return new RestHighLevelClient(
      RestClient.builder(
        new HttpHost(uri.getHost(),uri.getPort(),uri.getScheme()))
        .setRequestConfigCallback(reqConfigCallback));
  }


  @Bean
  public ElasticsearchOperations elasticsearchRestTemplate() {
    return new ElasticsearchRestTemplate(elasticsearchClient());
  }
}
