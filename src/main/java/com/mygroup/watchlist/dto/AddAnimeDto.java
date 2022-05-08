package com.mygroup.watchlist.dto;

public class AddAnimeDto extends AnimeStringDataDto {

  private byte[] picture;

  public AddAnimeDto() {}

  public AddAnimeDto(String title, String description, byte[] picture) {
    super(title, description);
    this.picture = picture;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }
}
