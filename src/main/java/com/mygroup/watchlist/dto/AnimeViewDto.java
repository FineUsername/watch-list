package com.mygroup.watchlist.dto;

import java.io.ByteArrayInputStream;

public class AnimeViewDto extends AnimeStringDataDto {

  private long id;
  private ByteArrayInputStream picture;
  private String statusRepresentation;

  public AnimeViewDto() {}

  public AnimeViewDto(long id, String title, String description, ByteArrayInputStream picture,
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

  public ByteArrayInputStream getPicture() {
    return picture;
  }

  public void setPicture(ByteArrayInputStream picture) {
    this.picture = picture;
  }

  public String getStatusRepresentation() {
    return statusRepresentation;
  }

  public void setStatusRepresentation(String statusRepresentation) {
    this.statusRepresentation = statusRepresentation;
  }

}
