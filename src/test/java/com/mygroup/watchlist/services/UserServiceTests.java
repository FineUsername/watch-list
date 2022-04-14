package com.mygroup.watchlist.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import com.mygroup.watchlist.entities.User;
import com.mygroup.watchlist.exceptions.UsernameAlreadyTakenException;
import com.mygroup.watchlist.forms.RegistrationForm;
import com.mygroup.watchlist.repositories.UserRepository;
import com.mygroup.watchlist.security.Role;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String ENCODED_PASSWORD_MOCK = "gibberish";
  private RegistrationForm commonForm = new RegistrationForm();

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @BeforeEach
  public void setup() {
    commonForm.setUsername(USERNAME);
    commonForm.setPassword(PASSWORD);
    commonForm.setConfirmPassword(PASSWORD);
  }

  @Test
  public void registerWhenAlreadyExists() {
    when(userRepository.existsByUsername(commonForm.getUsername())).thenReturn(true);
    var exception =
        assertThrows(UsernameAlreadyTakenException.class, () -> userService.register(commonForm));
    assertTrue(exception.getMessage().contains(commonForm.getUsername()));
  }

  @Test
  public void registerCommonCase() {
    when(userRepository.existsByUsername(commonForm.getUsername())).thenReturn(false);
    when(passwordEncoder.encode(commonForm.getPassword())).thenReturn(ENCODED_PASSWORD_MOCK);
    when(userRepository.save(any())).thenAnswer((arg) -> arg.getArgument(0));
    User user = userService.register(commonForm);
    assertEquals(user.getUsername(), commonForm.getUsername());
    assertEquals(user.getPassword(), ENCODED_PASSWORD_MOCK);
    assertEquals(user.getAuthorities(),
        Collections.singleton(new SimpleGrantedAuthority(Role.USER.getPrefixedName())));
  }
}
