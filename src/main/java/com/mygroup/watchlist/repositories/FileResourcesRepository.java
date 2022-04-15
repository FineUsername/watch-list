package com.mygroup.watchlist.repositories;

import com.mygroup.watchlist.exceptions.RepositoryException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

@Repository
public class FileResourcesRepository {

  private static final String RESOURCE_FOLDER = "src/main/resources/static";

  public String save(MultipartFile file, String fullLocation, String extension) {
    try {
      String fileName = System.currentTimeMillis() + extension;
      Path pathToSave = Paths.get(RESOURCE_FOLDER + '/' + fullLocation + '/' + fileName);
      Files.createDirectories(pathToSave.getParent());
      file.transferTo(pathToSave);
      return fileName;
    } catch (Exception e) {
      throw new RepositoryException(e);
    }
  }

}
