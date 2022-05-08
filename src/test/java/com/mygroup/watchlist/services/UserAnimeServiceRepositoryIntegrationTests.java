package com.mygroup.watchlist.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.entities.UserAnimeRelation;
import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.services.UserAnimeService;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(UserAnimeService.class)
public class UserAnimeServiceRepositoryIntegrationTests {

  private User user;
  private Anime anime;
  private UserAnimeRelation relation;

  @Autowired
  private UserAnimeService service;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  public void setup() {
    user = new User("username", "password", "email");
    anime = new Anime("title", "description", "picname");
    relation = new UserAnimeRelation(user, anime, WatchStatus.WATCHED);
    entityManager.persist(relation);
  }

  @Test
  public void getStatusWhenNoneExist() {
    assertThrows(NoSuchElementException.class, () -> service.getStatus(0L, 0L));
  }

  @Test
  public void getStatusCommonCase() {
    assertEquals(relation.getStatus(), service.getStatus(user.getId(), anime.getId()));
  }

  @Test
  public void updateStatusWhenNoneExist() {
    // Test that no exceptions are thrown
    service.updateStatus(0L, 0L, WatchStatus.PLANNED);
  }

  @Test
  public void updateStatusCommonCase() {
    service.updateStatus(user.getId(), anime.getId(), WatchStatus.PLANNED);
    assertEquals(WatchStatus.PLANNED,
        entityManager.find(UserAnimeRelation.class, relation.getId()).getStatus());
  }
}
