package com.joshir.els.exceptions;

public class OrderQueryClientException extends RuntimeException{
  public OrderQueryClientException(){super();}
  public OrderQueryClientException(String msg) {super(msg);}
  public OrderQueryClientException(String msg, Throwable t) {super(msg, t);}
}
