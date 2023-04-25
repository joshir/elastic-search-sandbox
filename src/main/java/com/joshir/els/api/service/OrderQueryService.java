package com.joshir.els.api.service;

import com.joshir.els.api.model.QueryResponse;
import com.joshir.els.client.OrderClient;
import com.joshir.els.configurations.ElasticSearchProps;
import com.joshir.els.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class OrderQueryService  implements QueryService{
  private final Mapper mapper;
  private final OrderClient orderClient;
  private final ElasticSearchProps elasticSearchProps;

  public OrderQueryService(Mapper mapper, OrderClient orderClient, ElasticSearchProps elasticSearchProps) {
    this.mapper = mapper;
    this.orderClient = orderClient;
    this.elasticSearchProps = elasticSearchProps;
  }

  @Override
  public QueryResponse getDocumentById(String id) {
    log.info("querying with id={} in index={}",id,elasticSearchProps.getIndex());
    return mapper
      .toQueryResponse(orderClient.getDocumentById(id));
  }

  @Override
  public List<QueryResponse> getDocumentByDescription(String desc) {
    log.info("querying with description={} for field={} in index={}.",desc, elasticSearchProps.getField(),elasticSearchProps.getIndex());
    return mapper
      .toListQueryResponse(orderClient.getDocumentByDescription(desc));
  }

  @Override
  public List<QueryResponse> getDocuments() {
    log.info("querying for all documents in index={}.",elasticSearchProps.getIndex());
    return mapper
      .toListQueryResponse(orderClient.getDocuments());
  }
}
