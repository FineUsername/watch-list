package com.mygroup.watchlist.exceptions;

public class UsernameAlreadyTakenException extends RuntimeException {

  private static final String MESSAGE = "Username %s is already taken";

  public UsernameAlreadyTakenException(String username) {
    super(String.format(MESSAGE, username));
  }

  public UsernameAlreadyTakenException(String username, Throwable cause) {
    super(String.format(MESSAGE, username), cause);
  }

}
