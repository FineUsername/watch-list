package com.mygroup.watchlist.back.config;

import com.vaadin.flow.spring.annotation.SpringComponent;
import java.nio.file.Path;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringComponent
@ConfigurationProperties("application.pictures")
public class PicturesProperties {

  private String extension;

  private Path userFolder;

  private Path animeFolder;

  public String getExtension() {
    return extension;
  }

  public void setExtension(String extension) {
    this.extension = extension;
  }

  public Path getUserFolder() {
    return userFolder;
  }

  public void setUserFolder(String userFolder) {
    this.userFolder = Path.of(userFolder);
  }

  public Path getAnimeFolder() {
    return animeFolder;
  }

  public void setAnimeFolder(String animeFolder) {
    this.animeFolder = Path.of(animeFolder);
  }
}
