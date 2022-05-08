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

  Optional<Anime> findByTitle(String title);

  boolean existsByTitle(String title);

  @Query("SELECT a FROM Anime a WHERE UPPER(a.title) LIKE UPPER(CONCAT('%', ?1, '%')) ORDER BY a.title ASC")
  Slice<Anime> findByTitlePart(String titlePart, Pageable page);

  @Query("SELECT new com.mygroup.watchlist.dto.AnimeWithStatusDto(a.id, a.title, a.description, a.pictureName, uar.status) "
      + "FROM Anime a LEFT JOIN a.userAnimeRelations uar ON uar.user.id = ?2 AND uar.anime.id = a.id "
      + "WHERE UPPER(a.title) LIKE UPPER(CONCAT('%', ?1, '%')) ORDER BY a.title ASC")
  Slice<AnimeWithStatusDto> findByTitlePartForUser(String titlePart, long userId, Pageable page);

  @Query("SELECT new com.mygroup.watchlist.dto.AnimeWithStatusDto(a.id, a.title, a.description, a.pictureName, uar.status) "
      + "FROM Anime a LEFT JOIN a.userAnimeRelations uar ON uar.user.id = ?2 AND uar.anime.id = a.id "
      + "WHERE UPPER(a.title) LIKE UPPER(CONCAT('%', ?1, '%')) AND uar.status IN ?3 ORDER BY a.title ASC")
  Slice<AnimeWithStatusDto> findByTitlePartForUserWithStatuses(String titlePart, long userId,
      Set<WatchStatus> statuses, Pageable page);

}
