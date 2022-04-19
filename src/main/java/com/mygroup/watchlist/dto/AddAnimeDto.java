package com.mygroup.watchlist.dto;

public class AddAnimeDto {

  private String title;
  private String description;
  private byte[] previewPicture;
  private byte[] mainPicture;

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

  public byte[] getPreviewPicture() {
    return previewPicture;
  }

  public void setPreviewPicture(byte[] previewPicture) {
    this.previewPicture = previewPicture;
  }

  public byte[] getMainPicture() {
    return mainPicture;
  }

  public void setMainPicture(byte[] mainPicture) {
    this.mainPicture = mainPicture;
  }
}
