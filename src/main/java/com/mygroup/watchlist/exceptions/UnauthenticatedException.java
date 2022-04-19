package com.mygroup.watchlist.exceptions;

public class UnauthenticatedException extends RuntimeException {

  public UnauthenticatedException() {
    super();
  }

  public UnauthenticatedException(String message, Throwable cause) {
    super(message, cause);
  }

  public UnauthenticatedException(String message) {
    super(message);
  }

  public UnauthenticatedException(Throwable cause) {
    super(cause);
  }

}
