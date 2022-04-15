package com.mygroup.watchlist.services;

import com.mygroup.watchlist.entities.Anime;
import com.mygroup.watchlist.exceptions.RepositoryException;
import com.mygroup.watchlist.exceptions.TitleAlreadyPresentException;
import com.mygroup.watchlist.forms.AnimeForm;
import com.mygroup.watchlist.repositories.AnimeRepository;
import com.mygroup.watchlist.repositories.FileResourcesRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("animeService")
public class AnimeService {

  private static final String PREVIEW_PICTURE_FOLDER = "images/anime/previews";
  private static final String MAIN_PICTURE_FOLDER = "images/anime/main";
  private static final String PICTURES_EXTENSION = ".jpg";

  private final AnimeRepository animeRepository;
  private final FileResourcesRepository fileResourcesRepository;

  @Autowired
  public AnimeService(AnimeRepository animeRepository,
      FileResourcesRepository fileResourcesRepository) {
    this.animeRepository = animeRepository;
    this.fileResourcesRepository = fileResourcesRepository;
  }

  public static String getPreviewPictureFolder() {
    return PREVIEW_PICTURE_FOLDER;
  }

  public static String getMainPictureFolder() {
    return MAIN_PICTURE_FOLDER;
  }

  public Anime add(AnimeForm animeForm) {
    if (animeRepository.existsByTitle(animeForm.getTitle())) {
      throw new TitleAlreadyPresentException(animeForm.getTitle());
    }
    Anime anime = new Anime();
    anime.setTitle(animeForm.getTitle());
    anime.setDescription(animeForm.getDescription());
    try {
      anime.setPreviewPicturePath(fileResourcesRepository.save(animeForm.getPreviewPicture(),
          PREVIEW_PICTURE_FOLDER, PICTURES_EXTENSION));
      anime.setMainPicturePath(fileResourcesRepository.save(animeForm.getMainPicture(),
          MAIN_PICTURE_FOLDER, PICTURES_EXTENSION));
      return animeRepository.save(anime);
    } catch (RepositoryException e) {
      throw e;
      // TODO handle
    }
  }

  public Anime findById(long id) {
    return animeRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  public List<Anime> findAll() {
    return animeRepository.findAll();
  }

  public List<Anime> searchTitle(String string) {
    return animeRepository.searchTitle(string);
  }

  public void deleteById(long id) {
    animeRepository.deleteById(id);
  }
}
