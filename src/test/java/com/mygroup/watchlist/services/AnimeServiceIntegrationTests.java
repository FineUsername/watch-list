package com.mygroup.watchlist.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.entities.UserAnimeRelation;
import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.repositories.FileResourcesRepository;
import com.mygroup.watchlist.back.services.AnimeService;
import com.mygroup.watchlist.dto.AddAnimeDto;
import com.mygroup.watchlist.dto.AnimeViewDto;
import com.mygroup.watchlist.exceptions.TitleAlreadyPresentException;
import com.mygroup.watchlist.exceptions.UnauthenticatedException;
import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@DataJpaTest
@Import(AnimeServiceIntegrationTestsConfig.class)
public class AnimeServiceIntegrationTests {

  private static final String DESCRIPTION = "Description does not matter for now,"
      + "because currently we don't have any operations that use it directly,"
      + "so for simplicity all test entities will use this sufficiently long description.";
  private static final String PICTURE_NAME = "Same logic as description";
  private static final String BERSERK_TITLE = "Berserk";
  private static final String DEATH_NOTE_TITLE = "Death note";
  private static final String COWBOY_BEBOP_TITLE = "Cowboy Bebop";
  private Anime berserk;
  private Anime deathNote;
  private Anime cowboyBebop;
  private User user1;
  private User user2;

  @MockBean
  private FileResourcesRepository fileResourcesRepository;

  @Autowired
  private AnimeService animeService;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  public void setup() {
    berserk = new Anime(BERSERK_TITLE, DESCRIPTION, PICTURE_NAME);
    deathNote = new Anime(DEATH_NOTE_TITLE, DESCRIPTION, PICTURE_NAME);
    cowboyBebop = new Anime(COWBOY_BEBOP_TITLE, DESCRIPTION, PICTURE_NAME);
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

  private static User createRandomUser() {
    User user = new User(UUID.randomUUID().toString(), "pass", UUID.randomUUID().toString());
    return user;
  }

  @Test
  public void addNullArg() {
    assertThrows(IllegalArgumentException.class, () -> animeService.add(null));
  }

  @Test
  public void addWhenTitleAlreadyPresent() {
    AddAnimeDto dto = new AddAnimeDto(BERSERK_TITLE, DESCRIPTION, new byte[1]);
    assertThrows(TitleAlreadyPresentException.class, () -> animeService.add(dto));
  }

  @Test
  public void addCommonCase() {
    String newTitle = "new title";
    AddAnimeDto dto = new AddAnimeDto(newTitle, DESCRIPTION, new byte[1]);
    String picName = "new picture name";
    when(fileResourcesRepository.save(any(), any(), any())).thenReturn(picName);
    Anime addedAnime = animeService.add(dto);
    assertEquals(newTitle, addedAnime.getTitle());
    assertEquals(DESCRIPTION, addedAnime.getDescription());
    assertEquals(picName, addedAnime.getPictureName());
  }

  @Test
  public void getAnimeViewByIdWhenNonExist() {
    assertThrows(NoSuchElementException.class, () -> animeService.getAnimeViewById(0L));
  }

  @Test
  public void getAnimeViewByIdCommonCase() throws Exception {
    byte[] pic = new byte[3];
    when(fileResourcesRepository.read(any(), any())).thenReturn(new ByteArrayInputStream(pic));
    AnimeViewDto dto = animeService.getAnimeViewById(berserk.getId());
    assertEquals(BERSERK_TITLE, dto.getTitle());
    assertEquals(DESCRIPTION, dto.getDescription());
    assertTrue(Arrays.equals(pic, dto.getPicture().readAllBytes()));
    assertTrue(dto.getStatusRepresentation().isEmpty());
  }

  @Test
  public void searchByTitleNullArg() {
    assertThrows(IllegalArgumentException.class,
        () -> animeService.searchByTitle(null, Pageable.ofSize(1)));
    assertThrows(IllegalArgumentException.class, () -> animeService.searchByTitle("e", null));
  }

  @Test
  public void searchByTitleWhenNoneExist() {
    int size = 2;
    Pageable page = Pageable.ofSize(size);
    List<AnimeViewDto> dtos = animeService.searchByTitle("gibberish", page).getContent();
    assertTrue(dtos.isEmpty());
  }

  @Test
  public void searchByTitleCommonCase() {
    int size = 2;
    Pageable page = Pageable.ofSize(size);
    List<AnimeViewDto> dtos = animeService.searchByTitle("e", page).getContent();
    assertEquals(size, dtos.size());
    assertEquals(BERSERK_TITLE, dtos.get(0).getTitle());
    assertEquals(COWBOY_BEBOP_TITLE, dtos.get(1).getTitle());
    assertTrue(dtos.get(0).getStatusRepresentation().isEmpty());
    assertTrue(dtos.get(1).getStatusRepresentation().isEmpty());
  }

  @Test
  public void searchByTitleForCurrentUserNullArg() {
    assertThrows(IllegalArgumentException.class,
        () -> animeService.searchByTitleForCurrentUser(null, Pageable.ofSize(1)));
    assertThrows(IllegalArgumentException.class,
        () -> animeService.searchByTitleForCurrentUser("e", null));
  }

  @Test
  public void searchByTitleForCurrentUserUnauthenticated() {
    assertThrows(UnauthenticatedException.class,
        () -> animeService.searchByTitleForCurrentUser("e", Pageable.ofSize(1)));
  }

  @Test
  public void searchByTitleForCurrentUserWhenNoneExist() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword()));
    int size = 2;
    Pageable page = Pageable.ofSize(size);
    List<AnimeViewDto> dtos =
        animeService.searchByTitleForCurrentUser("gibberish", page).getContent();
    assertTrue(dtos.isEmpty());
  }

  @Test
  public void searchByTitleForCurrentUserCommonCase() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword()));
    int size = 2;
    Pageable page = Pageable.ofSize(size);
    List<AnimeViewDto> dtos = animeService.searchByTitleForCurrentUser("e", page).getContent();
    assertEquals(size, dtos.size());
    assertEquals(BERSERK_TITLE, dtos.get(0).getTitle());
    assertEquals(COWBOY_BEBOP_TITLE, dtos.get(1).getTitle());
    assertEquals(WatchStatus.PLANNED.getStringRepresentation(),
        dtos.get(0).getStatusRepresentation());
    assertTrue(dtos.get(1).getStatusRepresentation().isEmpty());
  }

  @Test
  public void searchByTitleForCurrentUserNoStatuses() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(user2.getUsername(), user2.getPassword()));
    int size = 2;
    Pageable page = Pageable.ofSize(size);
    List<AnimeViewDto> dtos = animeService.searchByTitleForCurrentUser("e", page).getContent();
    assertEquals(size, dtos.size());
    assertEquals(BERSERK_TITLE, dtos.get(0).getTitle());
    assertEquals(COWBOY_BEBOP_TITLE, dtos.get(1).getTitle());
    assertTrue(dtos.get(0).getStatusRepresentation().isEmpty());
    assertTrue(dtos.get(1).getStatusRepresentation().isEmpty());
  }

  @Test
  public void searchByTitleForCurrentUserWithStatusesNullArg() {
    assertThrows(IllegalArgumentException.class,
        () -> animeService.searchByTitleForCurrentUserWithStatuses(null,
            Set.of(WatchStatus.values()), Pageable.ofSize(1)));
    assertThrows(IllegalArgumentException.class,
        () -> animeService.searchByTitleForCurrentUserWithStatuses("e", null, Pageable.ofSize(1)));
    assertThrows(IllegalArgumentException.class, () -> animeService
        .searchByTitleForCurrentUserWithStatuses("e", Set.of(WatchStatus.values()), null));
  }

  @Test
  public void searchByTitleForCurrentUserWithStatusesUnauthenticated() {
    assertThrows(UnauthenticatedException.class,
        () -> animeService.searchByTitleForCurrentUserWithStatuses("e",
            Set.of(WatchStatus.values()), Pageable.ofSize(1)));
  }

  @Test
  public void searchByTitleForCurrentUserWithStatusesCommonCase() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword()));
    int size = 2;
    Pageable page = Pageable.ofSize(size);
    List<AnimeViewDto> dtos =
        animeService.searchByTitleForCurrentUserWithStatuses("e", Set.of(WatchStatus.PLANNED), page)
            .getContent();
    assertEquals(1, dtos.size());
    assertEquals(BERSERK_TITLE, dtos.get(0).getTitle());
    assertEquals(WatchStatus.PLANNED.getStringRepresentation(),
        dtos.get(0).getStatusRepresentation());
  }

  @Test
  public void searchByTitleForCurrentUserWithStatusesWhenNoneExist() {
    SecurityContextHolder.getContext().setAuthentication(
        new UsernamePasswordAuthenticationToken(user1.getUsername(), user1.getPassword()));
    int size = 2;
    Pageable page = Pageable.ofSize(size);
    List<AnimeViewDto> dtos =
        animeService.searchByTitleForCurrentUserWithStatuses("e", Set.of(WatchStatus.STOPPED), page)
            .getContent();
    assertTrue(dtos.isEmpty());
  }

  @Test
  public void deleteById() {
    assertNotNull(entityManager.find(Anime.class, berserk.getId()));
    animeService.deleteById(berserk.getId());
    assertNull(entityManager.find(Anime.class, berserk.getId()));
  }
}
