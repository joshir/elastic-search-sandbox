package com.joshir.els.configurations;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Data
@Configuration
@ConfigurationProperties(prefix="el-search-config-props")
public class ElasticSearchProps {
  private String connUri;
  private int connTimeOutMs;
  private int socketTimeOutMs;
  private String index;
  private String isRepo;
  private String field;
  private String version;
}
