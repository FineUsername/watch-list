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
  private Anime anime1;
  private Anime anime2;
  private UserAnimeRelation relation;

  @Autowired
  private UserAnimeService service;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  public void setup() {
    user = new User("username", "password", "email");
    anime1 = new Anime("title", "description", new byte[1]);
    anime2 = new Anime("another title", "description", new byte[1]);
    relation = new UserAnimeRelation(user, anime1, WatchStatus.WATCHED);
    entityManager.persist(relation);
    entityManager.persist(anime2);
  }

  @Test
  public void getStatusWhenNoneExist() {
    assertThrows(NoSuchElementException.class, () -> service.getStatus(0L, 0L));
  }

  @Test
  public void getStatusCommonCase() {
    assertEquals(relation.getStatus(), service.getStatus(user.getId(), anime1.getId()));
  }

  @Test
  public void updateStatusWhenNoneExist() {
    service.updateStatus(user.getId(), anime2.getId(), WatchStatus.PLANNED);
    assertEquals(WatchStatus.PLANNED, service.getStatus(user.getId(), anime2.getId()));
  }

  @Test
  public void updateStatusCommonCase() {
    service.updateStatus(user.getId(), anime1.getId(), WatchStatus.PLANNED);
    assertEquals(WatchStatus.PLANNED,
        entityManager.find(UserAnimeRelation.class, relation.getId()).getStatus());
  }
}
