package com.joshir.els.mapper;

import com.joshir.els.api.model.QueryResponse;
import com.joshir.els.domain.OrderIndex;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class Mapper {

  public QueryResponse toQueryResponse(OrderIndex order) {
    return QueryResponse.builder()
      .id(order.getId())
      .description(order.getDescription())
      .userId(order.getUserId())
      .createdAt(order.getCreatedAt())
      .build();
  }

  public List<QueryResponse> toListQueryResponse(List<OrderIndex> orders) {
    return orders
      .stream()
      .map(this::toQueryResponse)
      .collect(Collectors.toList());
  }
}
