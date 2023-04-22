package com.joshir.els.client;

import com.joshir.els.configurations.ElasticSearchProps;
import com.joshir.els.domain.OrderIndex;
import com.joshir.els.utils.DocConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Slf4j
@ConditionalOnProperty(name="el-search-config-props.is-repo", havingValue="false")
public class OrderIndexingClient implements ElasticIndexingClient<OrderIndex> {
  private final ElasticsearchOperations elasticsearchOperations;
  private final ElasticSearchProps elasticSearchProps;
  private final DocConverter<OrderIndex> orderIndexDocConverter;

  public OrderIndexingClient(ElasticsearchOperations elasticsearchOperations, ElasticSearchProps elasticSearchProps, DocConverter<OrderIndex> orderIndexDocConverter) {
    this.elasticsearchOperations = elasticsearchOperations;
    this.elasticSearchProps = elasticSearchProps;
    this.orderIndexDocConverter = orderIndexDocConverter;
  }

  @Override
  public List<String> save(List<OrderIndex> orders) {
    List<IndexQuery> queries = orderIndexDocConverter.getIndexQueries(orders);
    IndexCoordinates coordinates = IndexCoordinates.of(elasticSearchProps.getIndex());

    List<String> ids = elasticsearchOperations
      .bulkIndex(queries, coordinates)
      .stream()
      .map(IndexedObjectInformation::getId)
      .collect(Collectors.toList());

    log.info("batched index for doc ids {} ...",ids.stream().limit(5) );
    return ids;
  }
}
