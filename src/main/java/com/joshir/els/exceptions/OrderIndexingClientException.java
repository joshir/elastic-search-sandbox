package com.joshir.els.exceptions;

public class OrderIndexingClientException extends RuntimeException{
  public OrderIndexingClientException(){super();}
  public OrderIndexingClientException(String msg) {super(msg);}
  public OrderIndexingClientException(String msg, Throwable t) {super(msg, t);}
}
