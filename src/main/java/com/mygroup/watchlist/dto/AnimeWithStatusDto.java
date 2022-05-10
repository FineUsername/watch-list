package com.mygroup.watchlist.dto;

import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;

public class AnimeWithStatusDto extends AnimeStringDataDto {

  private long id;
  private byte[] picture;
  private WatchStatus status;

  public AnimeWithStatusDto() {}

  public AnimeWithStatusDto(long id, String title, String description, byte[] picture,
      WatchStatus status) {
    super(title, description);
    this.id = id;
    this.picture = picture;
    this.status = status;
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

  public WatchStatus getStatus() {
    return status;
  }

  public void setStatus(WatchStatus status) {
    this.status = status;
  }


}
