package com.mygroup.watchlist.dto;

import com.mygroup.watchlist.back.entities.UserAnime.WatchStatus;
import java.io.InputStream;
import org.springframework.core.io.Resource;

public class AnimePreviewWithStatusDto extends AnimePreviewDto {

  private WatchStatus status;

  public AnimePreviewWithStatusDto(String title, String description, Resource previewPicture,
      WatchStatus status) {
    super(title, description, previewPicture);
    this.status = status;
  }

  public WatchStatus getStatus() {
    return status;
  }

  public void setStatus(WatchStatus status) {
    this.status = status;
  }

}
