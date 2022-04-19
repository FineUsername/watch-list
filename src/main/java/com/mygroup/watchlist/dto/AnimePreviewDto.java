package com.mygroup.watchlist.dto;

import java.io.InputStream;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class AnimePreviewDto {

  private String title;
  private String description;
  private Resource previewPicture;

  public AnimePreviewDto() {}

  public AnimePreviewDto(String title, String description, Resource previewPicture) {
    this.title = title;
    this.description = description;
    this.previewPicture = previewPicture;
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

  public Resource getPreviewPicture() {
    return previewPicture;
  }

  public void setPreviewPicture(Resource previewPicture) {
    this.previewPicture = previewPicture;
  }

}
