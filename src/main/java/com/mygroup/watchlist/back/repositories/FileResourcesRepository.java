package com.mygroup.watchlist.back.repositories;

import com.mygroup.watchlist.exceptions.RepositoryException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

@Repository
public class FileResourcesRepository {

  private static final String RESOURCE_FOLDER = "src/main/resources/META-INF/resources";

  public String save(byte[] content, String fullLocation, String extension) {
    try {
      String fileName = System.currentTimeMillis() + extension;
      Path pathToSave = Paths.get(RESOURCE_FOLDER + '/' + fullLocation + '/' + fileName);
      Files.createDirectories(pathToSave.getParent());
      Files.write(pathToSave, content);
      return fileName;
    } catch (Exception e) {
      throw new RepositoryException(e);
    }
  }

  public void delete(String fullLocation) {
    try {
      Files.delete(Paths.get(RESOURCE_FOLDER + '/' + fullLocation));
    } catch (Exception e) {
      // TODO handle
      throw new RepositoryException(e);
    }
  }

  public FileSystemResource find(String fullLocation) {
    try {
      return new FileSystemResource(Paths.get(RESOURCE_FOLDER + '/' + fullLocation));
    } catch (Exception e) {
      throw new RepositoryException(e);
    }
  }

}
