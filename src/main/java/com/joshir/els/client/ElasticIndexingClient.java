package com.joshir.els.client;

import com.joshir.els.Indexable;

import java.util.List;

public interface ElasticIndexingClient <T extends Indexable> {
  List<String> save (List<T> indexes);
}
