package com.joshir.els.client;

import com.joshir.els.Indexable;

import java.util.List;

public interface ElasticQueryClient <T extends Indexable> {
  T getDocumentById(String id);
  List<T> getDocumentByDescription(String desc);
  List<T> getDocuments();
}
