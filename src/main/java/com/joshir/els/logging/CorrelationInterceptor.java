package com.joshir.els.logging;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.UUID;

import static com.joshir.els.logging.LoggingConstants.CORRELATION_ID_LOG_VAR_NAME;
import static com.joshir.els.logging.LoggingConstants.CORRELATION_ID_HEADER_NAME;

@Component
public class CorrelationInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception{
    final String correlationId = getCorrelationIdFromHeader(request);
    MDC.put(CORRELATION_ID_LOG_VAR_NAME, correlationId);
    return true;
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
    MDC.remove(CORRELATION_ID_LOG_VAR_NAME);
  }

  private String getCorrelationIdFromHeader(HttpServletRequest request){
    String correlationId = request.getHeader(CORRELATION_ID_HEADER_NAME);
    return  StringUtils.isEmpty(correlationId)? generateUniqueCorrelationId() : correlationId;
  }

  private String generateUniqueCorrelationId() {
    return UUID.randomUUID().toString();
  }
}
