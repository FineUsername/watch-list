package com.mygroup.watchlist.forms;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

public class AnimeForm {

  @NotEmpty(message = "Title cannot be empty")
  private String title;

  @NotEmpty(message = "Description cannot be empty")
  private String description;

  private MultipartFile previewPicture;

  private MultipartFile mainPicture;

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

  public MultipartFile getPreviewPicture() {
    return previewPicture;
  }

  public void setPreviewPicture(MultipartFile previewPicture) {
    this.previewPicture = previewPicture;
  }

  public MultipartFile getMainPicture() {
    return mainPicture;
  }

  public void setMainPicture(MultipartFile mainPicture) {
    this.mainPicture = mainPicture;
  }

  @AssertTrue(message = "Preview picture cannot be empty")
  public boolean isPreviewPictureLoaded() {
    return (previewPicture != null) && !previewPicture.isEmpty();
  }

  @AssertTrue(message = "Main picture cannot be empty")
  public boolean isMainPictureLoaded() {
    return (mainPicture != null) && !mainPicture.isEmpty();
  }

}
