package com.joshir.els.mapper.exceptions;
public class MappingException extends RuntimeException {
  public MappingException(String msg) {
    super(msg);
  }
  public MappingException(String msg, Throwable t) {
    super(msg, t);
  }
  public MappingException(Throwable t) {
    super(t);
  }
}
