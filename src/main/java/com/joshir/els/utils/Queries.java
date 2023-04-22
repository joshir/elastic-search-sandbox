package com.joshir.els.utils;

import com.joshir.els.Indexable;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.elasticsearch.client.erhlc.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class Queries <T extends Indexable>{
  public Query getQueryById(String id) {
    return new NativeSearchQueryBuilder()
      .withIds(Collections.singleton(id))
      .build();
  }

  public Query getQueryByDescription(String field ,String desc) {
    return new NativeSearchQueryBuilder()
      .withQuery(new BoolQueryBuilder().must(QueryBuilders.matchQuery(field, desc)))
      .build();
  }

  public Query getSearchAll() {
    return new NativeSearchQueryBuilder()
      .withQuery(new BoolQueryBuilder().must(QueryBuilders.matchAllQuery()))
      .build();
  }
}
