package com.mygroup.watchlist.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import com.mygroup.watchlist.entities.User;
import com.mygroup.watchlist.security.Role;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@DataJpaTest
public class UserRepositoryTests {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private User user = new User();

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  public void setup() {
    user.setUsername(USERNAME);
    user.setPassword(PASSWORD);
    user.setRole(Role.USER);
    entityManager.persist(user);
  }
  
  @Test
  public void defaultProfilePicturePath() {
    assertEquals(userRepository.findByUsername(USERNAME).get().getProfilePicturePath(), "default-picture.png");
  }

  @Test
  public void existsByUsernameWhenNoneExist() {
    assertFalse(userRepository.existsByUsername("gibberish"));
  }

  @Test
  public void existsByUsernameWhenUsernameExists() {
    assertTrue(userRepository.existsByUsername(USERNAME));
  }

  @Test
  public void findByUsernameWhenNoneExist() {
    assertTrue(userRepository.findByUsername("gibberish").isEmpty());
  }

  @Test
  public void findByUsernameWhenUsernameExists() {
    User user = userRepository.findByUsername(USERNAME).orElseThrow();
    assertEquals(user.getUsername(), USERNAME);
    assertEquals(user.getPassword(), PASSWORD);
    assertEquals(user.getAuthorities(),
        Collections.singleton(new SimpleGrantedAuthority(Role.USER.getPrefixedName())));
  }
}
