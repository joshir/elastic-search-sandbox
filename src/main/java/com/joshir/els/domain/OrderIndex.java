package com.joshir.els.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.joshir.els.Indexable;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;

@Data
@Document(indexName = "#{elasticSearchProps.index}")
public class OrderIndex implements Indexable {
  @JsonProperty
  private String id;
  @JsonProperty
  private String userId;
  @JsonProperty
  private String description;
  @Field(type = FieldType.Date, format = DateFormat.basic_date_time)
  @JsonFormat(shape= JsonFormat.Shape.STRING, pattern = "uuuuMMdd'T'HHmmss.SSSXXX")
  @JsonProperty
  private LocalDateTime createdAt;
}
