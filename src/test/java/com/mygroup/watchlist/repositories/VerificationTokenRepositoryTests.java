package com.mygroup.watchlist.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.entities.VerificationToken;
import com.mygroup.watchlist.back.repositories.VerificationTokenRepository;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class VerificationTokenRepositoryTests {

  private User user;
  private VerificationToken token;

  @Autowired
  private VerificationTokenRepository tokenRepository;

  @Autowired
  private EntityManager entityManager;

  @BeforeEach
  public void setup() {
    user = new User("name", "pass", "email");
    token = new VerificationToken(user);
    user.setVerificationToken(token);
    entityManager.persist(user);
  }

  @Test
  public void findByBodyWhenNoneExist() {
    assertTrue(tokenRepository.findByBody(UUID.randomUUID()).isEmpty());
  }

  @Test
  public void findByBodyWhenExists() {
    Optional<VerificationToken> tokenOptional = tokenRepository.findByBody(token.getBody());
    assertTrue(tokenOptional.isPresent());
    assertEquals(token.getBody(), tokenOptional.get().getBody());
  }

  @Test
  public void existsByBodyWhenNoneExist() {
    assertFalse(tokenRepository.existsByBody(UUID.randomUUID()));
  }

  @Test
  public void existsByBodyWhenExists() {
    assertTrue(tokenRepository.existsByBody(token.getBody()));
  }

  @Test
  public void deleteByBodyWhenNoneExist() {
    long initialCount = tokenRepository.count();
    tokenRepository.deleteByBody(UUID.randomUUID());
    assertEquals(initialCount, tokenRepository.count());
  }

  @Test
  public void deleteByBodyWhenExists() {
    assertTrue(tokenRepository.existsByBody(token.getBody()));
    assertFalse(user.isEnabled());
    tokenRepository.deleteByBody(token.getBody());
    assertFalse(tokenRepository.existsByBody(token.getBody()));
    assertTrue(user.isEnabled());
  }
}
