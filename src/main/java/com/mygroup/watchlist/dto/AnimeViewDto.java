package com.mygroup.watchlist.dto;

public class AnimeViewDto extends AnimeStringDataDto {

  private long id;
  private byte[] picture;

  public AnimeViewDto() {}

  public AnimeViewDto(long id, String title, String description, byte[] picture) {
    super(title, description);
    this.id = id;
    this.picture = picture;
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
}
