package com.joshir.els.exceptions;

public class OrderClientException extends RuntimeException{
  public OrderClientException(Throwable t){super(t);}
  public OrderClientException(String msg) {super(msg);}
  public OrderClientException(String msg, Throwable t) {super(msg, t);}
}
