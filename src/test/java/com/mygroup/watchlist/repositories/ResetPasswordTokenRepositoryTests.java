package com.mygroup.watchlist.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.mygroup.watchlist.back.entities.ResetPasswordToken;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.repositories.ResetPasswordTokenRepository;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class ResetPasswordTokenRepositoryTests {

  private User user;
  private ResetPasswordToken token;

  @Autowired
  private ResetPasswordTokenRepository tokenRepository;

  @Autowired
  private EntityManager entityManager;

  @BeforeEach
  public void setup() {
    user = new User("name", "pass", "email");
    token = new ResetPasswordToken(user);
    user.setResetPasswordToken(token);
    entityManager.persist(user);
  }

  @Test
  public void findByBodyWhenNoneExist() {
    assertTrue(tokenRepository.findByBody(UUID.randomUUID()).isEmpty());
  }

  @Test
  public void findByBodyWhenExists() {
    Optional<ResetPasswordToken> tokenOptional = tokenRepository.findByBody(token.getBody());
    assertTrue(tokenOptional.isPresent());
    assertEquals(token.getBody(), tokenOptional.get().getBody());
  }

}
