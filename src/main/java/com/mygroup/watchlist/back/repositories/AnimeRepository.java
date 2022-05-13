package com.mygroup.watchlist.back.repositories;

import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.dto.AnimeWithStatusDto;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

  /**
   * Returns optional with entity with the given title, or Optional.empty() if none found.
   * 
   * @param title
   * @return Optional with entity with the given title, or Optional.empty() if none found.
   */
  Optional<Anime> findByTitle(String title);

  /**
   * Returns whether an entity with the title exists.
   * 
   * @param title
   * @return true if an entity with the given username exists, false otherwise.
   */
  boolean existsByTitle(String title);

  /**
   * Returns slice of animes which title contain the given title part. Check is performed
   * case-insensitively. Resulting slice is ordered by title ascendingly.
   * 
   * @param titlePart
   * @param page
   * @return {@literal Slice<Anime>} in which all animes contain the given title part in their
   *         titles.
   */
  @Query("SELECT a FROM Anime a WHERE UPPER(a.title) LIKE UPPER(CONCAT('%', ?1, '%')) ORDER BY a.title ASC")
  Slice<Anime> findByTitlePart(String titlePart, Pageable page);

  /**
   * Returns slice of AnimeWithStatusDto made from animes which title contain the given title part.
   * Check is performed case-insensitively. Resulting slice is ordered by title ascendingly. If
   * anime has UserAnimeRelation with user with the given id, resulting dto contains status of that
   * relation. Otherwise, null is stored in resulting dto's status field.
   * 
   * @param titlePart
   * @param userId - for every anime which has a UserAnimeRelation with user with the given id, a
   *        status of that relations will be retrieved.
   * @param page
   * @return {@literal Slice<AnimeWithStatusDto>} in which all dtos contain given title part in
   *         their titles. If relation between anime and user with the given id was found, dto
   *         contains status of that relation. Otherwise, dto contains null in status field.
   */
  @Query("SELECT new com.mygroup.watchlist.dto.AnimeWithStatusDto(a.id, a.title, a.description, a.picture, uar.status) "
      + "FROM Anime a LEFT JOIN a.userAnimeRelations uar ON uar.user.id = ?2 AND uar.anime.id = a.id "
      + "WHERE UPPER(a.title) LIKE UPPER(CONCAT('%', ?1, '%')) ORDER BY a.title ASC")
  Slice<AnimeWithStatusDto> findByTitlePartForUser(String titlePart, long userId, Pageable page);

  /**
   * Returns slice of AnimeWithStatusDto made from animes which conform to the conditions: 1) anime
   * contains the given title part in title (case-insensitively) 2) anime has an UserAnimeRelation
   * with user with the given id 3) status of that relation is present in given statuses. Resulting
   * slice is ordered by title ascendingly.
   * 
   * @param titlePart
   * @param userId - only animes that have a UserAnimeRelations with user with the given id will be
   *        returned.
   * @param statuses - relation will be ignored if its status is not contained in statuses.
   * @param page
   * @return {@literal Slice<AnimeWithStatusDto>}
   */
  @Query("SELECT new com.mygroup.watchlist.dto.AnimeWithStatusDto(a.id, a.title, a.description, a.picture, uar.status) "
      + "FROM Anime a LEFT JOIN a.userAnimeRelations uar ON uar.user.id = ?2 AND uar.anime.id = a.id "
      + "WHERE UPPER(a.title) LIKE UPPER(CONCAT('%', ?1, '%')) AND uar.status IN ?3 ORDER BY a.title ASC")
  Slice<AnimeWithStatusDto> findByTitlePartForUserWithStatuses(String titlePart, long userId,
      Set<WatchStatus> statuses, Pageable page);

}
