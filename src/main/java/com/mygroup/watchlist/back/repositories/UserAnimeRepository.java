package com.mygroup.watchlist.back.repositories;

import com.mygroup.watchlist.back.entities.UserAnimeRelation;
import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAnimeRepository extends JpaRepository<UserAnimeRelation, Long> {

  /**
   * Returns whether an entity with the given user id and anime id exists.
   * 
   * @param userId
   * @param animeId
   * @return true if an entity with the given user id and anime id exists, false otherwise.
   */
  boolean existsByUserIdAndAnimeId(long userId, long animeId);

  /**
   * Returns optional with entity with the given user id and anime id, or Optional.empty() if none
   * found.
   * 
   * @param userId
   * @param animeId
   * @return Optional with entity with the given user id and anime id,, or Optional.empty() if none
   *         found.
   */
  @Query("SELECT uar FROM UserAnimeRelation uar WHERE uar.user.id = ?1 AND uar.anime.id = ?2")
  Optional<UserAnimeRelation> findByUserIdAndAnimeId(long userId, long animeId);

  /**
   * Updates status of UserAnimeRelation with the given user id and anime id.
   * 
   * @param userId
   * @param animeId
   * @param status new status for relation
   */
  @Transactional
  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query("UPDATE UserAnimeRelation uar SET uar.status = ?3 "
      + "WHERE uar.user.id = ?1 AND uar.anime.id = ?2")
  void updateStatus(long userId, long animeId, WatchStatus status);
}
