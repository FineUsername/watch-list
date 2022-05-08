package com.mygroup.watchlist.exceptions;

public class FileOperationException extends RuntimeException {

  public FileOperationException() {
    super();
  }

  public FileOperationException(String message) {
    super(message);
  }

  public FileOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileOperationException(Throwable cause) {
    super(cause);
  }

}
