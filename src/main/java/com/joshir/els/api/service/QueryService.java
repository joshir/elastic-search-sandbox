package com.joshir.els.api.service;

import com.joshir.els.api.model.QueryResponse;

import java.util.List;

public interface QueryService {
  QueryResponse getDocumentById(String id);
  List<QueryResponse> getDocumentByDescription(String desc);
  List<QueryResponse> getDocuments();

}
