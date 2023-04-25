package com.joshir.els.exceptions;

public class MappingException extends RuntimeException{
  public MappingException(Throwable t){super(t);}
  public MappingException(String msg) {super(msg);}
  public MappingException(String msg, Throwable t) {super(msg, t);}
}
