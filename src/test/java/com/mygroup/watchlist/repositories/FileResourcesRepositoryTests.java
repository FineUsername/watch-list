package com.mygroup.watchlist.repositories;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import com.google.common.jimfs.Configuration;
import com.google.common.jimfs.Jimfs;
import com.mygroup.watchlist.back.config.ResourcesProperties;
import com.mygroup.watchlist.back.repositories.FileResourcesRepository;
import com.mygroup.watchlist.exceptions.FileOperationException;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class FileResourcesRepositoryTests {

  private static final String EXTENSION = ".jpg";
  private Path locationInResources;
  private byte[] content;
  private FileSystem fileSystem;
  private Path resourceFolderPath;

  @Mock
  private ResourcesProperties resourcesProperties;

  @InjectMocks
  private FileResourcesRepository fileResourcesRepository;

  @BeforeEach
  public void setup() throws IOException {
    int contentSize = 10000;
    content = new byte[contentSize];
    for (int i = 0; i < contentSize; i++) {
      content[i] = (byte) (i % 255);
    }
    fileSystem = Jimfs.newFileSystem(Configuration.unix());
    resourceFolderPath = fileSystem.getPath("");
    locationInResources = resourceFolderPath.resolve("folder");
  }

  @Test
  public void saveNullContent() {
    assertThrows(FileOperationException.class,
        () -> fileResourcesRepository.save(null, locationInResources, EXTENSION));
  }

  @Test
  public void saveNullLocation() {
    assertThrows(FileOperationException.class,
        () -> fileResourcesRepository.save(content, null, EXTENSION));
  }

  @Test
  public void saveNullExtension() {
    assertThrows(FileOperationException.class,
        () -> fileResourcesRepository.save(content, locationInResources, null));
  }

  @Test
  public void readNullLocation() {
    assertThrows(FileOperationException.class,
        () -> fileResourcesRepository.read(null, "filename"));
  }

  @Test
  public void readNullFilename() {
    assertThrows(FileOperationException.class,
        () -> fileResourcesRepository.read(locationInResources, null));
  }

  @Test
  public void deleteNullLocation() {
    assertThrows(FileOperationException.class,
        () -> fileResourcesRepository.delete(null, "filename"));
  }

  @Test
  public void deleteNullFilename() {
    assertThrows(FileOperationException.class,
        () -> fileResourcesRepository.delete(locationInResources, null));
  }

  @Test
  public void saveAndReadAndDelete() throws Exception {
    when(resourcesProperties.getFolder()).thenReturn(resourceFolderPath);
    String fileName = fileResourcesRepository.save(content, locationInResources, EXTENSION);
    Path fullPath = resourceFolderPath.resolve(locationInResources).resolve(fileName);
    assertTrue(Files.exists(fullPath));

    byte[] actualContent =
        fileResourcesRepository.read(locationInResources, fileName).readAllBytes();
    assertTrue(Arrays.equals(actualContent, content));

    fileResourcesRepository.delete(locationInResources, fileName);
    assertFalse(Files.exists(fullPath));
  }
}
