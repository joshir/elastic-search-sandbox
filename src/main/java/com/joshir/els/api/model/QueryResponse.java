package com.joshir.els.api.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class QueryResponse {
  private String id;
  private String userId;
  private String description;
  private LocalDateTime createdAt;
}
