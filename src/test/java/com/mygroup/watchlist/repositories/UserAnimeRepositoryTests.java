package com.mygroup.watchlist.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.entities.UserAnimeRelation;
import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.repositories.UserAnimeRepository;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class UserAnimeRepositoryTests {

  private User user;
  private Anime anime;
  private UserAnimeRelation userAnimeRelation;

  @Autowired
  private UserAnimeRepository userAnimeRepository;

  @Autowired
  private EntityManager entityManager;

  @BeforeEach
  public void setup() {
    user = new User("username", "password", "email");
    anime = new Anime("title", "description", new byte[1]);
    userAnimeRelation = new UserAnimeRelation(user, anime, WatchStatus.PLANNED);
    entityManager.persist(userAnimeRelation);
  }

  @Test
  public void findByUserIdAndAnimeIdWhenNoneExist() {
    assertTrue(userAnimeRepository.findByUserIdAndAnimeId(0L, 0L).isEmpty());
  }

  @Test
  public void findByUserIdAndAnimeIdWhenExists() {
    assertTrue(userAnimeRepository.findByUserIdAndAnimeId(user.getId(), anime.getId()).isPresent());
  }

  @Test
  public void updateStatus() {
    WatchStatus newStatus = WatchStatus.WATCHED;
    userAnimeRepository.updateStatus(user.getId(), anime.getId(), newStatus);
    assertEquals(newStatus,
        userAnimeRepository.findById(userAnimeRelation.getId()).get().getStatus());
  }
}
