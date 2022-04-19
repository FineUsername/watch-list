package com.mygroup.watchlist.utils;

import com.vaadin.flow.server.InputStreamFactory;
import java.io.InputStream;

public class InputStreamFactoryImpl implements InputStreamFactory {

  private InputStream inputStream;

  public InputStreamFactoryImpl(InputStream inputStream) {
    this.inputStream = inputStream;
  }

  @Override
  public InputStream createInputStream() {
    return inputStream;
  }

}
