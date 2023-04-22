package com.joshir.els.repository;

import com.joshir.els.domain.OrderIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderIndexingRepository extends ElasticsearchRepository<OrderIndex, String> {
}
