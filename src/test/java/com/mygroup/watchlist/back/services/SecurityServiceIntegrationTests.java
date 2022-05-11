package com.mygroup.watchlist.back.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import com.mygroup.watchlist.back.config.PasswordEncoderConfig;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.repositories.UserRepository;
import com.mygroup.watchlist.back.security.Role;
import com.mygroup.watchlist.dto.UsernamePasswordDto;
import com.mygroup.watchlist.dto.UsernamePasswordEmailDto;
import com.mygroup.watchlist.exceptions.EmailAlreadyPresentException;
import com.mygroup.watchlist.exceptions.UnauthenticatedException;
import com.mygroup.watchlist.exceptions.UsernameAlreadyTakenException;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
@Import({SecurityService.class, PasswordEncoderConfig.class})
public class SecurityServiceIntegrationTests {

  private static final String USERNAME = "username";
  private static final String NEW_USERNAME = "new-username";
  private static final String PASSWORD = "password";
  private static final String NEW_PASSWORD = "new-password";
  private static final String EMAIL = "email@email.email";
  private static final String NEW_EMAIL = "em@a.il";
  private User user;

  @MockBean
  private EmailService emailService;

  @SpyBean
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private SecurityService securityService;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  public void setup() {
    user = new User(USERNAME, passwordEncoder.encode(PASSWORD), EMAIL);
    entityManager.persist(user);
  }

  @Test
  public void loginNullArg() {
    assertThrows(IllegalArgumentException.class, () -> securityService.login(null));
  }

  @Test
  public void loginBadUsername() {
    UsernamePasswordDto dto = new UsernamePasswordDto("gibberish", PASSWORD);
    assertThrows(BadCredentialsException.class, () -> securityService.login(dto));
  }

  @Test
  public void loginBadPassword() {
    UsernamePasswordDto dto = new UsernamePasswordDto(USERNAME, "gibberish");
    assertThrows(BadCredentialsException.class, () -> securityService.login(dto));
  }

  @Test
  public void loginSuccess() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertTrue(
        (authentication == null) || (authentication instanceof AnonymousAuthenticationToken));
    UsernamePasswordDto dto = new UsernamePasswordDto(USERNAME, PASSWORD);
    securityService.login(dto);
    authentication = SecurityContextHolder.getContext().getAuthentication();
    assertTrue(!(authentication instanceof AnonymousAuthenticationToken));
    assertEquals(USERNAME, authentication.getName());
    assertTrue(passwordEncoder.matches(PASSWORD, authentication.getCredentials().toString()));
  }

  @Test
  public void logoutSuccess() {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD));
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    assertTrue(!(authentication instanceof AnonymousAuthenticationToken));
    securityService.logout(new MockHttpServletRequest());
    authentication = SecurityContextHolder.getContext().getAuthentication();
    assertTrue(
        (authentication == null) || (authentication instanceof AnonymousAuthenticationToken));
  }

  @Test
  public void registerNullArg() {
    assertThrows(IllegalArgumentException.class, () -> securityService.registerAsUser(null));
    assertThrows(IllegalArgumentException.class, () -> securityService.registerAsAdmin(null));
  }

  @Test
  public void registerWhenEmailPresent() {
    UsernamePasswordEmailDto dto = new UsernamePasswordEmailDto(NEW_USERNAME, NEW_PASSWORD, EMAIL);
    assertThrows(EmailAlreadyPresentException.class, () -> securityService.registerAsUser(dto));
    assertThrows(EmailAlreadyPresentException.class, () -> securityService.registerAsAdmin(dto));
  }

  @Test
  public void registerWhenUsernameTaken() {
    UsernamePasswordEmailDto dto = new UsernamePasswordEmailDto(USERNAME, NEW_PASSWORD, NEW_EMAIL);
    assertThrows(UsernameAlreadyTakenException.class, () -> securityService.registerAsUser(dto));
    assertThrows(UsernameAlreadyTakenException.class, () -> securityService.registerAsAdmin(dto));
  }

  @Test
  public void registerAsUserCommonCase() {
    clearInvocations(userRepository);
    UsernamePasswordEmailDto dto =
        new UsernamePasswordEmailDto(NEW_USERNAME, NEW_PASSWORD, NEW_EMAIL);
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    securityService.registerAsUser(dto);
    verify(emailService).sendConfirmRegistrationEmail(eq(NEW_EMAIL), anyString());
    verify(userRepository).save(userCaptor.capture());
    User registeredUser = userCaptor.getValue();
    assertEquals(NEW_USERNAME, registeredUser.getUsername());
    assertTrue(passwordEncoder.matches(NEW_PASSWORD, registeredUser.getPassword()));
    assertEquals(NEW_EMAIL, registeredUser.getEmail());
    assertEquals(Collections.singleton(new SimpleGrantedAuthority(Role.USER.getPrefixedName())),
        registeredUser.getAuthorities());
    assertNotNull(registeredUser.getVerificationToken());
  }

  @Test
  public void registerAsAdminCommonCase() {
    clearInvocations(userRepository);
    UsernamePasswordEmailDto dto =
        new UsernamePasswordEmailDto(NEW_USERNAME, NEW_PASSWORD, NEW_EMAIL);
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    securityService.registerAsAdmin(dto);
    verify(emailService, times(0)).sendConfirmRegistrationEmail(eq(NEW_EMAIL), anyString());
    verify(userRepository).save(userCaptor.capture());
    User registeredUser = userCaptor.getValue();
    assertEquals(NEW_USERNAME, registeredUser.getUsername());
    assertTrue(passwordEncoder.matches(NEW_PASSWORD, registeredUser.getPassword()));
    assertEquals(NEW_EMAIL, registeredUser.getEmail());
    assertEquals(Collections.singleton(new SimpleGrantedAuthority(Role.ADMIN.getPrefixedName())),
        registeredUser.getAuthorities());
    assertNull(registeredUser.getVerificationToken());
  }

  @Test
  public void getCurrentlyAuthenticatedUserWhenUnauthenticated() {
    assertThrows(UnauthenticatedException.class,
        () -> securityService.getCurrentlyAuthenticatedUser());
  }

  @Test
  public void getCurrentlyAuthenticatedUserSuccess() {
    SecurityContextHolder.getContext()
        .setAuthentication(new UsernamePasswordAuthenticationToken(USERNAME, PASSWORD));
    User authenticatedUser = securityService.getCurrentlyAuthenticatedUser();
    assertEquals(USERNAME, authenticatedUser.getUsername());
    assertTrue(passwordEncoder.matches(PASSWORD, authenticatedUser.getPassword()));
  }
}
