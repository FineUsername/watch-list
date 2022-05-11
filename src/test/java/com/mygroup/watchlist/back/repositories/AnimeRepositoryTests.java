package com.mygroup.watchlist.back.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.entities.UserAnimeRelation;
import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.dto.AnimeWithStatusDto;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

@DataJpaTest
public class AnimeRepositoryTests {

  private static final String DESCRIPTION = "Description does not matter for now,"
      + "because currently we don't have any operations that use it directly,"
      + "so for simplicity all test entities will use this sufficiently long description.";
  private static final String BERSERK_TITLE = "Berserk";
  private static final String DEATH_NOTE_TITLE = "Death note";
  private static final String COWBOY_BEBOP_TITLE = "Cowboy Bebop";
  private Anime berserk;
  private Anime deathNote;
  private Anime cowboyBebop;
  private User user1;
  private User user2;

  @Autowired
  private AnimeRepository animeRepository;

  @Autowired
  private EntityManager entityManager;

  @BeforeEach
  public void setup() {
    berserk = createAnime(BERSERK_TITLE);
    deathNote = createAnime(DEATH_NOTE_TITLE);
    cowboyBebop = createAnime(COWBOY_BEBOP_TITLE);
    user1 = createRandomUser();
    user2 = createRandomUser();
    entityManager.persist(berserk);
    entityManager.persist(deathNote);
    entityManager.persist(cowboyBebop);
    entityManager.persist(user1);
    entityManager.persist(user2);
    entityManager.persist(new UserAnimeRelation(user1, berserk, WatchStatus.PLANNED));
    entityManager.persist(new UserAnimeRelation(user1, deathNote, WatchStatus.WATCHED));
  }

  private static Anime createAnime(String title) {
    return new Anime(title, DESCRIPTION, new byte[1]);
  }

  private static User createRandomUser() {
    return new User(UUID.randomUUID().toString(), "pass", UUID.randomUUID().toString());
  }

  @Test
  public void findByTitleWhenNoneExist() {
    assertTrue(animeRepository.findByTitle("gibberish").isEmpty());
  }

  @Test
  public void findByTitleWhenTitleExists() {
    assertTrue(animeRepository.findByTitle(BERSERK_TITLE).isPresent());
  }

  @Test
  public void existsByTitleWhenNoneExist() {
    assertFalse(animeRepository.existsByTitle("gibberish"));
  }

  @Test
  public void existsByTitleWhenTitleExists() {
    assertTrue(animeRepository.existsByTitle(BERSERK_TITLE));
  }

  @Test
  public void findByTitlePartWhenNoneExist() {
    int pageSize = 2;
    Pageable page = Pageable.ofSize(pageSize);
    Slice<Anime> animes = animeRepository.findByTitlePart("gibberish", page);
    assertTrue(animes.isEmpty());
  }

  @Test
  public void findByTitlePartWhenMultipleExist() {
    int pageSize = 2;
    Pageable page = Pageable.ofSize(pageSize);
    List<Anime> animes = animeRepository.findByTitlePart("e", page).toList();
    assertEquals(pageSize, animes.size());
    assertEquals(BERSERK_TITLE, animes.get(0).getTitle());
    assertEquals(COWBOY_BEBOP_TITLE, animes.get(1).getTitle());
  }

  @Test
  public void findByTitlePartForUserWhenNoneExist() {
    int pageSize = 1;
    Pageable page = Pageable.ofSize(pageSize);
    Slice<AnimeWithStatusDto> animes =
        animeRepository.findByTitlePartForUser("gibberish", user1.getId(), page);
    assertTrue(animes.isEmpty());
  }

  @Test
  public void findByTitlePartForUserWhenMultipleExist() {
    int pageSize = 1;
    Pageable page = Pageable.ofSize(pageSize);
    List<AnimeWithStatusDto> animes =
        animeRepository.findByTitlePartForUser("e", user1.getId(), page).toList();
    assertEquals(pageSize, animes.size());
    assertEquals(BERSERK_TITLE, animes.get(0).getTitle());
    assertEquals(DESCRIPTION, animes.get(0).getDescription());
    assertEquals(WatchStatus.PLANNED, animes.get(0).getStatus());
    page = page.next();
    animes = animeRepository.findByTitlePartForUser("e", user1.getId(), page).toList();
    assertEquals(pageSize, animes.size());
    assertEquals(COWBOY_BEBOP_TITLE, animes.get(0).getTitle());
    assertEquals(null, animes.get(0).getStatus());
    page = page.next();
    animes = animeRepository.findByTitlePartForUser("e", user1.getId(), page).toList();
    assertEquals(pageSize, animes.size());
    assertEquals(DEATH_NOTE_TITLE, animes.get(0).getTitle());
    assertEquals(WatchStatus.WATCHED, animes.get(0).getStatus());
  }


  @Test
  public void findByTitlePartForUserWithStatusesByOneStatus() {
    int pageSize = 1;
    Pageable page = Pageable.ofSize(pageSize);
    Set<WatchStatus> statuses = Set.of(WatchStatus.WATCHED);
    List<AnimeWithStatusDto> animes = animeRepository
        .findByTitlePartForUserWithStatuses("e", user1.getId(), statuses, page).toList();
    assertEquals(pageSize, animes.size());
    assertEquals(DEATH_NOTE_TITLE, animes.get(0).getTitle());
    assertEquals(DESCRIPTION, animes.get(0).getDescription());
    assertEquals(WatchStatus.WATCHED, animes.get(0).getStatus());
    page = page.next();
    animes = animeRepository.findByTitlePartForUserWithStatuses("e", user1.getId(), statuses, page)
        .toList();
    assertTrue(animes.isEmpty());
  }

  @Test
  public void findByTitlePartForUserWithStatusesByAllStatuses() {
    int pageSize = 2;
    Pageable page = Pageable.ofSize(pageSize);
    Set<WatchStatus> statuses = Set.of(WatchStatus.values());
    List<AnimeWithStatusDto> animes = animeRepository
        .findByTitlePartForUserWithStatuses("e", user1.getId(), statuses, page).getContent();
    assertEquals(pageSize, animes.size());
    assertEquals(BERSERK_TITLE, animes.get(0).getTitle());
    assertEquals(DEATH_NOTE_TITLE, animes.get(1).getTitle());
    page = page.next();
    animes = animeRepository.findByTitlePartForUserWithStatuses("e", user1.getId(), statuses, page)
        .getContent();
    assertTrue(animes.isEmpty());
  }
}
