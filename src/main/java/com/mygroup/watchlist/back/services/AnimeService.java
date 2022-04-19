package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.repositories.AnimeRepository;
import com.mygroup.watchlist.back.repositories.FileResourcesRepository;
import com.mygroup.watchlist.dto.AddAnimeDto;
import com.mygroup.watchlist.dto.AnimePreviewDto;
import com.mygroup.watchlist.exceptions.TitleAlreadyPresentException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
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

  public Anime add(AddAnimeDto dto) {
    if (animeRepository.existsByTitle(dto.getTitle())) {
      throw new TitleAlreadyPresentException(dto.getTitle());
    }
    Anime anime = new Anime();
    anime.setTitle(dto.getTitle());
    anime.setDescription(dto.getDescription());
    anime.setPreviewPicturePath(fileResourcesRepository.save(dto.getPreviewPicture(),
        PREVIEW_PICTURE_FOLDER, PICTURES_EXTENSION));
    anime.setMainPicturePath(fileResourcesRepository.save(dto.getMainPicture(), MAIN_PICTURE_FOLDER,
        PICTURES_EXTENSION));
    return animeRepository.save(anime);
  }

  public FileSystemResource getPreviewPicture(Anime anime) {
    return fileResourcesRepository.find(getPreviewPictureUrl(anime));
  }

  public FileSystemResource getMainPicture(Anime anime) {
    return fileResourcesRepository.find(getMainPictureUrl(anime));
  }

  public Anime findById(long id) {
    return animeRepository.findById(id).orElseThrow(NoSuchElementException::new);
  }

  public List<Anime> findAll() {
    return animeRepository.findAll();
  }

  public List<AnimePreviewDto> findPreviewsLimitedRange(long limit, long offset) {
    return animeRepository.findLimitedRange(limit, offset).stream()
        .map(anime -> new AnimePreviewDto(anime.getTitle(), anime.getDescription(),
            fileResourcesRepository.find(getPreviewPictureUrl(anime))))
        .collect(Collectors.toList());
  }

  public List<Anime> searchTitleLimitedRange(String titlePart, long limit, long offset) {
    return animeRepository.searchTitleLimitedRange(titlePart, limit, offset);
  }

  public void deleteById(long id) {
    animeRepository.deleteById(id);
  }

  private String getPreviewPictureUrl(Anime anime) {
    return PREVIEW_PICTURE_FOLDER + '/' + anime.getPreviewPicturePath();
  }

  private String getMainPictureUrl(Anime anime) {
    return MAIN_PICTURE_FOLDER + '/' + anime.getMainPicturePath();
  }
}
