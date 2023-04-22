package com.joshir.els.client;

import com.joshir.els.domain.OrderIndex;
import com.joshir.els.repository.OrderIndexingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Slf4j
@ConditionalOnProperty(name="el-search-config-props.is-repo", havingValue="true",matchIfMissing=true)
public class OrderIndexingRepoClient implements ElasticIndexingClient<OrderIndex> {
  private final OrderIndexingRepository orderIndexingRepository;

  public OrderIndexingRepoClient(OrderIndexingRepository orderIndexingRepository) {
    this.orderIndexingRepository = orderIndexingRepository;
  }

  @Override
  public List<String> save(List<OrderIndex> indexes) {
    Iterator<OrderIndex> it = orderIndexingRepository.saveAll(indexes).iterator();
    List<OrderIndex> orders =
      StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false)
        .collect(Collectors.toList());
    List<String> ids = orders.stream().map(OrderIndex::getId).collect(Collectors.toList());
    log.info("batched index for doc ids {} ...", ids.stream().limit(5) );
    return ids;
  }
}
