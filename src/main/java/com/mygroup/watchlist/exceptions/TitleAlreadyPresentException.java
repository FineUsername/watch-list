package com.mygroup.watchlist.exceptions;

public class TitleAlreadyPresentException extends RuntimeException {

  private static final String MESSAGE = "Title %s is already present";

  public TitleAlreadyPresentException(String title, Throwable cause) {
    super(String.format(MESSAGE, title), cause);
  }

  public TitleAlreadyPresentException(String title) {
    super(String.format(MESSAGE, title));
  }

  public TitleAlreadyPresentException(Throwable cause) {
    super(cause);
  }

}
