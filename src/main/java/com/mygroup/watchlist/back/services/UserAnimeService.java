package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.entities.UserAnimeRelation;
import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.repositories.UserAnimeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAnimeService {

  private final UserAnimeRepository userAnimeRepository;

  @Autowired
  public UserAnimeService(UserAnimeRepository userAnimeRepository) {
    this.userAnimeRepository = userAnimeRepository;
  }

  /**
   * Gets status of relation with given user id and anime id.
   * 
   * @param userId
   * @param animeId
   * @throws NoSuchElementException if relation with that user id and anime id wasn't found
   * @return status
   */
  public WatchStatus getStatus(long userId, long animeId) {
    return findByUserIdAndAnimeId(userId, animeId).getStatus();
  }

  /**
   * Updates status of relation with given user id and anime id with given status.
   * 
   * @param userId
   * @param animeId
   * @param status
   */
  public void updateStatus(long userId, long animeId, WatchStatus status) {
    userAnimeRepository.updateStatus(userId, animeId, status);
  }

  private UserAnimeRelation findByUserIdAndAnimeId(long userId, long animeId) {
    return userAnimeRepository.findByUserIdAndAnimeId(userId, animeId).orElseThrow();
  }
}
