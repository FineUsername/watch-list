package com.mygroup.watchlist.front.components;

import com.vaadin.flow.component.HasValueAndElement;
import com.vaadin.flow.component.AbstractField.ComponentValueChangeEvent;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.internal.AbstractFieldSupport;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.dom.DomEvent;
import com.vaadin.flow.dom.DomEventListener;
import com.vaadin.flow.shared.Registration;
import java.io.IOException;
import java.util.Objects;

public class BasicUpload extends Upload
    implements HasValueAndElement<ComponentValueChangeEvent<BasicUpload, byte[]>, byte[]> {

  private MemoryBuffer buffer;

  private byte[] uploadedFile;

  private final AbstractFieldSupport<BasicUpload, byte[]> fieldSupport;

  public BasicUpload() {
    init();
    fieldSupport =
        new AbstractFieldSupport<>(this, null, Objects::deepEquals, obj -> uploadedFile = obj);
  }

  public BasicUpload(String labelText) {
    this();
    setDropLabel(new Label(labelText));
  }

  public boolean isEmpty() {
    return uploadedFile == null;
  }

  private void init() {
    buffer = new MemoryBuffer();
    setReceiver(buffer);
    addFinishedListener(event -> {
      try {
        uploadedFile = buffer.getInputStream().readAllBytes();
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    getElement().addEventListener("upload-abort", new DomEventListener() {
      @Override
      public void handleEvent(DomEvent event) {
        uploadedFile = null;
      }
    });
  }

  @Override
  public void setValue(byte[] value) {
    uploadedFile = value;
  }

  @Override
  public byte[] getValue() {
    return uploadedFile;
  }

  @Override
  public Registration addValueChangeListener(
      ValueChangeListener<? super ComponentValueChangeEvent<BasicUpload, byte[]>> listener) {
    return fieldSupport.addValueChangeListener(listener);
  }



}
