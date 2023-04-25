package com.joshir.els.api.service;

import com.joshir.els.api.model.QueryResponse;
import com.joshir.els.client.OrderClient;
import com.joshir.els.configurations.ElasticSearchProps;
import com.joshir.els.logging.LoggingConstants;
import com.joshir.els.mapper.Mapper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
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
    log.info("querying with id={} in index={} for requestId {}",id,elasticSearchProps.getIndex(),MDC.get(LoggingConstants.CORRELATION_ID_LOG_VAR_NAME));
    return mapper
      .toQueryResponse(orderClient.getDocumentById(id));
  }

  @Override
  public List<QueryResponse> getDocumentByDescription(String desc) {
    log.info("querying with description={} for field={} in index={} for requestId {}",desc, elasticSearchProps.getField(),elasticSearchProps.getIndex(),MDC.get(LoggingConstants.CORRELATION_ID_LOG_VAR_NAME));
    return mapper
      .toListQueryResponse(orderClient.getDocumentByDescription(desc));
  }

  @Override
  public List<QueryResponse> getDocuments() {
    log.info("querying for all documents in index={} for requestId {}",elasticSearchProps.getIndex(), MDC.get(LoggingConstants.CORRELATION_ID_LOG_VAR_NAME));
    return mapper
      .toListQueryResponse(orderClient.getDocuments());
  }
}
