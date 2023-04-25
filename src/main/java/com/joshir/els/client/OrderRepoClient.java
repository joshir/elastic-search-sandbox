package com.joshir.els.client;

import com.joshir.els.domain.OrderIndex;
import com.joshir.els.exceptions.OrderQueryClientException;
import com.joshir.els.mapper.JsonMapperHelper;
import com.joshir.els.repository.OrderIndexingRepository;
import com.joshir.els.repository.OrderQueryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Slf4j
@Service
@ConditionalOnProperty(name="el-search-config-props.is-repo", havingValue="true",matchIfMissing=true)
public class OrderRepoClient implements ElasticQueryClient<OrderIndex>, ElasticIndexingClient<OrderIndex> {
  private final OrderQueryRepository  orderQueryRepository;
  private final OrderIndexingRepository orderIndexingRepository;

  public OrderRepoClient(OrderQueryRepository orderQueryRepository, OrderIndexingRepository orderIndexingRepository) {
    this.orderQueryRepository = orderQueryRepository;
    this.orderIndexingRepository = orderIndexingRepository;
  }

  @Override
  public OrderIndex getDocumentById(String id) {
    OrderIndex order = orderQueryRepository.findById(id)
      .orElseThrow(()-> new OrderQueryClientException("document for id: "+id+" not found"));

    log.info("Found document: {} for id:", JsonMapperHelper.writeToJson(order),id);
    return order;
  }

  @Override
  public List<OrderIndex> getDocumentByDescription(String desc) {
    List<OrderIndex> orders =  orderQueryRepository.findByDescription(desc);
    log.info("Found document: {} for id:", JsonMapperHelper.writeToJson(orders), desc);
    return orders;
  }

  @Override
  public List<OrderIndex> getDocuments() {
    Iterator<OrderIndex> it = orderQueryRepository.findAll().iterator();
    List<OrderIndex> orders =
      StreamSupport.stream(Spliterators.spliteratorUnknownSize(it, Spliterator.ORDERED), false)
        .collect(Collectors.toList());
    log.info("batched index for doc ids {} ...", JsonMapperHelper.writeToJson(orders.stream().limit(5) ));
    return orders;
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
