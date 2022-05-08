package com.mygroup.watchlist.back.config;

import com.vaadin.flow.spring.annotation.SpringComponent;
import java.nio.file.Path;
import org.springframework.boot.context.properties.ConfigurationProperties;

@SpringComponent
@ConfigurationProperties("application.resources")
public class ResourcesProperties {

  private Path folder;

  public Path getFolder() {
    return folder;
  }

  public void setFolder(String folder) {
    this.folder = Path.of(folder);
  }
}
