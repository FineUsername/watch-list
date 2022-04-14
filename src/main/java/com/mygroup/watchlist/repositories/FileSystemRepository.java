package com.mygroup.watchlist.repositories;

import com.mygroup.watchlist.exceptions.RepositoryException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Repository;

@Repository
public class FileSystemRepository {

  public String save(byte[] content, String fullLocation, String extension) {
    try {
      Path pathToSave = Paths.get(fullLocation + System.currentTimeMillis() + extension);
      Files.createDirectories(pathToSave.getParent());
      Files.write(pathToSave, content);
      return pathToSave.toAbsolutePath().toString();
    } catch (Exception e) {
      throw new RepositoryException(e);
    }
  }

  public FileSystemResource findFile(String fullLocation) {
    try {
      return new FileSystemResource(fullLocation);
    } catch (Exception e) {
      throw new RepositoryException(e);
    }
  }
}
