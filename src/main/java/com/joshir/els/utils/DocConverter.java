package com.joshir.els.utils;

import com.joshir.els.Indexable;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class DocConverter<T extends Indexable>{
  public List<IndexQuery> getIndexQueries(List<T> indexes) {
    Stream<T> indexStream = indexes.stream();
    List<IndexQuery> queries = indexStream.map(
      indexable ->
        new IndexQueryBuilder()
        .withId(indexable.getId())
        .withObject(indexable).build()
    ).collect(Collectors.toList());

    return queries;
  }


}
