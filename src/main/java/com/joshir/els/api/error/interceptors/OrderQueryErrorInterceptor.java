package com.joshir.els.api.error.interceptors;

import com.joshir.els.exceptions.MappingException;
import com.joshir.els.logging.LoggingConstants;
import com.joshir.els.mapper.JsonMapperHelper;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
class OrderQueryErrorInterceptor {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handle(MethodArgumentNotValidException ex) {
    Map<String,String> errorMap = new HashMap<>(ex.getBindingResult().getErrorCount());
    ex.getBindingResult()
      .getAllErrors()
      .forEach(error -> errorMap.put(((FieldError)error).getField(), error.getDefaultMessage()));
    log.info("error: {}, trace:", JsonMapperHelper.writeToJson(errorMap), MDC.get(LoggingConstants.CORRELATION_ID_LOG_VAR_NAME));
    return ResponseEntity.badRequest().body(errorMap);
  }

  @ExceptionHandler(MappingException.class)
  public ResponseEntity<String> intercept(MappingException ex) {
    log.info("mapping error for trace {}", MDC.get(LoggingConstants.CORRELATION_ID_LOG_VAR_NAME));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error.");
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<String> intercept(IllegalArgumentException ex) {
    log.info("bad request error for trace {}", MDC.get(LoggingConstants.CORRELATION_ID_LOG_VAR_NAME));
    return ResponseEntity.badRequest().body("Bad Request.");
  }

  @ExceptionHandler({RuntimeException.class, Exception.class})
  public ResponseEntity<String> intercept(RuntimeException ex) {
    log.info("internal error for trace {}", MDC.get(LoggingConstants.CORRELATION_ID_LOG_VAR_NAME));
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Error.");
  }
}
