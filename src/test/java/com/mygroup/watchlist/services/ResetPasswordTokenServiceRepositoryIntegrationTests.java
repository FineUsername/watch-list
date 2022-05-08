package com.mygroup.watchlist.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import com.mygroup.watchlist.back.entities.ResetPasswordToken;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.services.ResetPasswordTokenService;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(ResetPasswordTokenService.class)
public class ResetPasswordTokenServiceRepositoryIntegrationTests {

  private User user;
  private ResetPasswordToken token;

  @Autowired
  private ResetPasswordTokenService service;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  public void setup() {
    user = new User("username", "password", "email");
    token = new ResetPasswordToken(user);
    user.setResetPasswordToken(token);
    entityManager.persist(user);
  }

  @Test
  public void findByBodyWhenNoneExist() {
    assertThrows(NoSuchElementException.class, () -> service.findByBody(UUID.randomUUID()));
  }

  @Test
  public void findByBodyCommonCase() {
    assertEquals(token.getBody(), service.findByBody(token.getBody()).getBody());
  }

  @Test
  public void deleteTokenCommonCase() {
    ResetPasswordToken foundToken = entityManager.find(ResetPasswordToken.class, token.getId());
    assertNotNull(foundToken);
    assertNotNull(user.getResetPasswordToken());
    service.deleteToken(token);
    foundToken = entityManager.find(ResetPasswordToken.class, token.getId());
    assertNull(foundToken);
    assertNull(user.getResetPasswordToken());
  }
}
