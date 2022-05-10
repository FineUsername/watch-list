package com.mygroup.watchlist.dto;

import java.io.InputStream;

public class AnimeViewDto extends AnimeStringDataDto {

  private long id;
  private byte[] picture;
  private String statusRepresentation;

  public AnimeViewDto() {}

  public AnimeViewDto(long id, String title, String description, byte[] picture,
      String statusRepresentation) {
    super(title, description);
    this.id = id;
    this.picture = picture;
    this.statusRepresentation = statusRepresentation;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  public String getStatusRepresentation() {
    return statusRepresentation;
  }

  public void setStatusRepresentation(String statusRepresentation) {
    this.statusRepresentation = statusRepresentation;
  }

}
