package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.repositories.AnimeRepository;
import com.mygroup.watchlist.dto.AddAnimeDto;
import com.mygroup.watchlist.dto.AnimePreviewDto;
import com.mygroup.watchlist.dto.AnimeViewDto;
import com.mygroup.watchlist.dto.AnimeWithStatusDto;
import com.mygroup.watchlist.exceptions.TitleAlreadyPresentException;
import java.io.ByteArrayInputStream;
import java.util.NoSuchElementException;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@Service
public class AnimeService {

  private final AnimeRepository animeRepository;
  private final SecurityService securityService;

  @Autowired
  public AnimeService(AnimeRepository animeRepository, SecurityService securityService) {
    this.animeRepository = animeRepository;
    this.securityService = securityService;
  }

  /**
   * Adds new anime constructed from given dto
   * 
   * @param dto
   * @return added anime
   * @throws IllegalArgumentException if dto is null
   * @throws TitleAlreadyPresentException
   * @throws FileOperationException if something goes wrong during picture save
   */
  public Anime add(AddAnimeDto dto) {
    if (dto == null) {
      throw new IllegalArgumentException();
    }
    if (animeRepository.existsByTitle(dto.getTitle())) {
      throw new TitleAlreadyPresentException(dto.getTitle());
    }
    Anime anime = new Anime();
    anime.setTitle(dto.getTitle());
    anime.setDescription(dto.getDescription());
    anime.setPicture(dto.getPicture());
    return animeRepository.save(anime);
  }

  /**
   * Gets AnimeViewDto for anime with given id and empty status.
   * 
   * @param id
   * @return AnimeViewDto for anime with given id
   * @throws NoSuchElementException if anime with given id wasn't found
   * @throws FileOperationException if something goes wrong during picture save
   */
  public AnimeViewDto getAnimeViewById(long id) {
    return convert(animeRepository.findById(id).orElseThrow(NoSuchElementException::new));
  }

  /**
   * Searches by title part using given pageable and converts found animes to AnimePreviewDto with
   * empty status. Result is ordered in ascending alphabetical order.
   * 
   * @param titlePart - a string titles will be searched for
   * @param page
   * @return Slice of AnimePreviewDto with empty status and title containing given title part
   * @throws IllegalArgumentException if any argument is null
   * @throws FileOperationException if something goes wrong during picture retrieval
   */
  public Slice<AnimePreviewDto> searchByTitle(String titlePart, Pageable page) {
    if ((titlePart == null) || (page == null)) {
      throw new IllegalArgumentException();
    }
    return animeRepository.findByTitlePart(titlePart, page).map(this::convertWithDefaultStatus);
  }

  /**
   * Searches by title part using given pageable and converts found animes to AnimePreviewDto with
   * status for current user. Result is ordered in ascending alphabetical order.
   * 
   * @param titlePart
   * @param page
   * @return Slice of AnimePreviewDto with status for current user and title containing given title
   *         part
   * @throws IllegalArgumentException if any argument is null
   * @throws FileOperationException if something goes wrong during picture retrieval
   * @throws UnauthenticatedException if user is not authenticated
   */
  public Slice<AnimePreviewDto> searchByTitleForCurrentUser(String titlePart, Pageable page) {
    if ((titlePart == null) || (page == null)) {
      throw new IllegalArgumentException();
    }
    User currentUser = securityService.getCurrentlyAuthenticatedUser();
    return animeRepository.findByTitlePartForUser(titlePart, currentUser.getId(), page)
        .map(this::convert);
  }

  /**
   * Searches by title part and by statuses for current user using given pageable and converts found
   * animes to AnimePreviewDto with status for current user. Result is ordered in ascending
   * alphabetical order.
   * 
   * @param titlePart
   * @param page
   * @param statuses - only animes with status that is present in this set will be returned
   * @return Slice of AnimePreviewDto with status only from given statuses for current user and
   *         title containing given title part
   * @throws IllegalArgumentException if any argument is null
   * @throws FileOperationException if something goes wrong during picture retrieval
   * @throws UnauthenticatedException if user is not authenticated
   */
  public Slice<AnimePreviewDto> searchByTitleForCurrentUserWithStatuses(String titlePart,
      Set<WatchStatus> statuses, Pageable page) {
    if ((titlePart == null) || (statuses == null) || (page == null)) {
      throw new IllegalArgumentException();
    }
    User currentUser = securityService.getCurrentlyAuthenticatedUser();
    return animeRepository
        .findByTitlePartForUserWithStatuses(titlePart, currentUser.getId(), statuses, page)
        .map(this::convert);
  }

  /**
   * Deletes the anime with the given id.
   * 
   * @param id must not be null
   * @throws IllegalArgumentException if the given id is null
   */
  public void deleteById(long id) {
    animeRepository.deleteById(id);
  }

  private AnimePreviewDto convertWithDefaultStatus(Anime anime) {
    return new AnimePreviewDto(anime.getId(), anime.getTitle(), anime.getDescription(),
        new ByteArrayInputStream(anime.getPicture()), "");
  }

  private AnimePreviewDto convert(AnimeWithStatusDto dto) {
    String statusRepresentation = "";
    if (dto.getStatus() != null) {
      statusRepresentation = dto.getStatus().getStringRepresentation();
    }
    return new AnimePreviewDto(dto.getId(), dto.getTitle(), dto.getDescription(),
        new ByteArrayInputStream(dto.getPicture()), statusRepresentation);
  }

  private AnimeViewDto convert(Anime anime) {
    return new AnimeViewDto(anime.getId(), anime.getTitle(), anime.getDescription(),
        anime.getPicture());
  }

}
