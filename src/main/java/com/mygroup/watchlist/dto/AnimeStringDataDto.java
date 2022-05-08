package com.mygroup.watchlist.dto;

public class AnimeStringDataDto {

  private String title;
  private String description;

  public AnimeStringDataDto() {}

  public AnimeStringDataDto(String title, String description) {
    this.title = title;
    this.description = description;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
