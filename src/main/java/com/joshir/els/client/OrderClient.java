package com.joshir.els.client;

import com.joshir.els.configurations.ElasticSearchProps;
import com.joshir.els.domain.OrderIndex;
import com.joshir.els.mapper.JsonMapperHelper;
import com.joshir.els.utils.DocConverter;
import com.joshir.els.utils.Queries;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.IndexedObjectInformation;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
@ConditionalOnProperty(name="el-search-config-props.is-repo", havingValue="false")
public class OrderClient implements ElasticQueryClient<OrderIndex>, ElasticIndexingClient<OrderIndex>{
  private final ElasticsearchOperations elasticsearchOperations;
  private final ElasticSearchProps elasticSearchProps;
  private final DocConverter<OrderIndex> orderIndexDocConverter;
  private final Queries queries;

  public OrderClient(ElasticsearchOperations elasticsearchOperations, ElasticSearchProps elasticSearchProps, DocConverter<OrderIndex> orderIndexDocConverter, Queries queries) {
    this.elasticsearchOperations = elasticsearchOperations;
    this.elasticSearchProps = elasticSearchProps;
    this.orderIndexDocConverter = orderIndexDocConverter;
    this.queries = queries;
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


  @Override
  public OrderIndex getDocumentById(String id) {
    SearchHit<OrderIndex> searchHit = elasticsearchOperations
            .searchOne(queries.getQueryById(id), OrderIndex.class, IndexCoordinates.of(elasticSearchProps.getIndex()));
    if(searchHit==null){
      log.error("no documents found for id: {}",id);
    }
    log.info("found {} for id: {}", JsonMapperHelper.writeToJson(searchHit.getContent()), id);
    return searchHit.getContent();
  }

  @Override
  public List<OrderIndex> getDocumentByDescription(String desc) {
    SearchHits<OrderIndex> searchHits = elasticsearchOperations.search(
            queries.getQueryByDescription(elasticSearchProps.getField(), desc),
            OrderIndex.class,
            IndexCoordinates.of(elasticSearchProps.getIndex()));
    Stream<SearchHit<OrderIndex>> searchHitsStream = searchHits.stream();
    List<OrderIndex> orders = searchHitsStream.map(SearchHit::getContent).collect(Collectors.toList());
    log.info("retrieved {} docs for field: {} with desc: {}",orders.size(), elasticSearchProps.getField(), desc);
    return orders;
  }

  @Override
  public List<OrderIndex> getDocuments() {
    SearchHits<OrderIndex> searchHits = elasticsearchOperations.search(
            queries.getSearchAll(),
            OrderIndex.class,
            IndexCoordinates.of(elasticSearchProps.getIndex()));
    Stream<SearchHit<OrderIndex>> searchHitsStream = searchHits.stream();
    List<OrderIndex> orders = searchHitsStream.map(SearchHit::getContent).collect(Collectors.toList());
    log.info("retrieved {} for search all ",orders.size());
    return orders;
  }
}
