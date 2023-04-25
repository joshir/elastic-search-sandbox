package com.joshir.els.repository;

import com.joshir.els.domain.OrderIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends ElasticsearchRepository<OrderIndex, String> {
  List<OrderIndex> findByDescription(String description);
}
