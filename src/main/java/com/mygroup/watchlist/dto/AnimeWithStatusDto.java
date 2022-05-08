package com.mygroup.watchlist.dto;

import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;

public class AnimeWithStatusDto extends AnimeStringDataDto {

  private long id;
  private String pictureName;
  private WatchStatus status;

  public AnimeWithStatusDto() {}

  public AnimeWithStatusDto(long id, String title, String description, String pictureName,
      WatchStatus status) {
    super(title, description);
    this.id = id;
    this.pictureName = pictureName;
    this.status = status;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getPictureName() {
    return pictureName;
  }

  public void setPictureName(String pictureName) {
    this.pictureName = pictureName;
  }

  public WatchStatus getStatus() {
    return status;
  }

  public void setStatus(WatchStatus status) {
    this.status = status;
  }


}
