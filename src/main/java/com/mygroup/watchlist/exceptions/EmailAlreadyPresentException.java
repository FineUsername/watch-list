package com.mygroup.watchlist.exceptions;

public class EmailAlreadyPresentException extends RuntimeException {

  private static final String MESSAGE = "Email %s is already present";

  public EmailAlreadyPresentException(String email) {
    super(String.format(MESSAGE, email));
  }

  public EmailAlreadyPresentException(String email, Throwable cause) {
    super(String.format(MESSAGE, email), cause);
  }

  public EmailAlreadyPresentException(Throwable cause) {
    super(cause);
  }
}
