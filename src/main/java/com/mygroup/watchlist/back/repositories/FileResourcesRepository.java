package com.mygroup.watchlist.back.repositories;

import com.mygroup.watchlist.back.config.ResourcesProperties;
import com.mygroup.watchlist.exceptions.FileOperationException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FileResourcesRepository {

  private final ResourcesProperties resourcesProperties;

  @Autowired
  public FileResourcesRepository(ResourcesProperties resourcesProperties) {
    this.resourcesProperties = resourcesProperties;
  }

  public String save(byte[] content, Path locationInResources, String extension) {
    try {
      if ((content == null) || (locationInResources == null) || (extension == null)) {
        throw new NullPointerException();
      }
      String fileName = getUniqueFilename(extension);
      Path pathToSave = buildPathInResources(locationInResources, fileName);
      Files.createDirectories(pathToSave.getParent());
      Files.write(pathToSave, content);
      return fileName;
    } catch (Exception e) {
      throw new FileOperationException(e);
    }
  }

  public void delete(Path locationInResources, String fileName) {
    try {
      if ((locationInResources == null) || (fileName == null)) {
        throw new NullPointerException();
      }
      Files.delete(buildPathInResources(locationInResources, fileName));
    } catch (Exception e) {
      throw new FileOperationException(e);
    }
  }

  public InputStream read(Path locationInResources, String fileName) {
    try {
      if ((locationInResources == null) || (fileName == null)) {
        throw new NullPointerException();
      }
      return Files.newInputStream(buildPathInResources(locationInResources, fileName));
    } catch (Exception e) {
      throw new FileOperationException(e);
    }
  }

  private Path buildPathInResources(Path locationInResources, String fileName) {
    return resourcesProperties.getFolder().resolve(locationInResources).resolve(fileName);
  }

  private static String getUniqueFilename(String extension) {
    return System.currentTimeMillis() + extension;
  }

}
