package com.mygroup.watchlist.services;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.entities.VerificationToken;
import com.mygroup.watchlist.back.services.VerificationTokenService;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(VerificationTokenService.class)
public class VerificationTokenServiceRepositoryIntegrationTests {

  private User user;
  private VerificationToken token;

  @Autowired
  private VerificationTokenService service;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  public void setup() {
    user = new User("username", "password", "email");
    token = new VerificationToken(user);
    user.setVerificationToken(token);
    entityManager.persist(user);
  }

  @Test
  public void deleteTokenWhenNoneExist() {
    assertThrows(NoSuchElementException.class, () -> service.deleteToken(UUID.randomUUID()));
  }

  @Test
  public void deleteTokenCommonCase() {
    VerificationToken foundToken = entityManager.find(VerificationToken.class, token.getId());
    assertNotNull(foundToken);
    service.deleteToken(token.getBody());
    foundToken = entityManager.find(VerificationToken.class, token.getId());
    assertNull(foundToken);
  }
}
