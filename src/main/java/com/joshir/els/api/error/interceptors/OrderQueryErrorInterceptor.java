package com.joshir.els.api.error.interceptors;

import co.elastic.clients.elasticsearch.nodes.Http;
import co.elastic.clients.util.Pair;
import com.joshir.els.mapper.JsonMapperHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
class OrderQueryErrorInterceptor {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException ex) {
    // TODO log.info("trace:");
    Map<String,String> errorMap = new HashMap<>(ex.getBindingResult().getErrorCount());
    ex.getBindingResult()
      .getAllErrors()
      .forEach(error -> errorMap.put(((FieldError)error).getField(), error.getDefaultMessage()));
    log.info("error: {}, trace:", JsonMapperHelper.writeToJson(errorMap));
    return ResponseEntity.badRequest().body(errorMap);
  }

  @ExceptionHandler(AccessDeniedException.class)
  public ResponseEntity<String> intercept(AccessDeniedException ex) {
    // TODO log.info("trace:");
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized.");
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> intercept(IllegalArgumentException ex) {
    // TODO log.info("trace:");
    return ResponseEntity.badRequest().body("Bad Request.");
  }

  @ExceptionHandler({RuntimeException.class, Exception.class})
  public ResponseEntity<String> intercept(RuntimeException ex) {
    // TODO log.info("trace:");
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error.");
  }

}
