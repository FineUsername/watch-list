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

  boolean existsByUserIdAndAnimeId(long userId, long animeId);

  @Query("SELECT uar FROM UserAnimeRelation uar WHERE uar.user.id = ?1 AND uar.anime.id = ?2")
  Optional<UserAnimeRelation> findByUserIdAndAnimeId(long userId, long animeId);

  @Transactional
  @Modifying(flushAutomatically = true, clearAutomatically = true)
  @Query("UPDATE UserAnimeRelation uar SET uar.status = ?3 "
      + "WHERE uar.user.id = ?1 AND uar.anime.id = ?2")
  void updateStatus(long userId, long animeId, WatchStatus status);
}
