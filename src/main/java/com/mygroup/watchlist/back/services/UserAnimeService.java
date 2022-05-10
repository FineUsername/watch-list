package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.entities.UserAnimeRelation;
import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.repositories.UserAnimeRepository;
import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserAnimeService {

  private final UserAnimeRepository userAnimeRepository;
  private final EntityManager entityManager;

  @Autowired
  public UserAnimeService(UserAnimeRepository userAnimeRepository, EntityManager entityManager) {
    this.userAnimeRepository = userAnimeRepository;
    this.entityManager = entityManager;
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
   * If relation with given userId and animeId exists, updates status of that relation with given
   * status. Otherwise creates new relation with given userId and animeId and sets given status.
   * 
   * @param userId
   * @param animeId
   * @param status
   * @throws EntityNotFoundException if user with that id wasn't found or anime with that id wasn't
   *         found
   */
  @Transactional
  public void updateStatus(long userId, long animeId, WatchStatus status) {
    if (userAnimeRepository.existsByUserIdAndAnimeId(userId, animeId)) {
      userAnimeRepository.updateStatus(userId, animeId, status);
    } else {
      User user = entityManager.getReference(User.class, userId);
      Anime anime = entityManager.getReference(Anime.class, animeId);
      userAnimeRepository.save(new UserAnimeRelation(user, anime, status));
    }
  }

  private UserAnimeRelation findByUserIdAndAnimeId(long userId, long animeId) {
    return userAnimeRepository.findByUserIdAndAnimeId(userId, animeId).orElseThrow();
  }
}
