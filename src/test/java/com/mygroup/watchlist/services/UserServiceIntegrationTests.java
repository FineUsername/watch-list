package com.mygroup.watchlist.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import com.mygroup.watchlist.back.entities.ResetPasswordToken;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.repositories.FileResourcesRepository;
import com.mygroup.watchlist.back.services.UserService;
import com.mygroup.watchlist.dto.ResetPasswordDto;
import com.mygroup.watchlist.dto.UsernamePasswordEmailDto;
import java.nio.file.Path;
import java.util.NoSuchElementException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

@DataJpaTest
@Import(UserServiceTestsConfig.class)
@ExtendWith(MockitoExtension.class)
public class UserServiceIntegrationTests {

  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";
  private static final String EMAIL = "email@email.email";
  private static final String NEW_PASSWORD = "new-password";
  private User user;

  @MockBean
  private JavaMailSender mailSender;

  @MockBean
  private FileResourcesRepository fileResourcesRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private TestEntityManager entityManager;

  @BeforeEach
  public void setup() {
    user = new User(USERNAME, PASSWORD, EMAIL);
    entityManager.persist(user);
  }

  @Test
  public void emailAlreadyTakenFalse() {
    assertFalse(userService.emailAlreadyTaken("gibberish"));
  }

  @Test
  public void emailAlreadyTakenTrue() {
    assertTrue(userService.emailAlreadyTaken(user.getEmail()));
  }

  @Test
  public void findByIdWhenNoneExist() {
    assertThrows(NoSuchElementException.class, () -> userService.findById(0L));
  }

  @Test
  public void findByIdWhenExists() {
    User found = userService.findById(user.getId());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getPassword(), user.getPassword());
    assertEquals(found.getEmail(), user.getEmail());
  }

  @Test
  public void findByUsernameWhenNoneExist() {
    assertThrows(UsernameNotFoundException.class, () -> userService.findByUsername("gibberish"));
  }

  @Test
  public void findByUsernameWhenExists() {
    User found = userService.findByUsername(user.getUsername());
    assertEquals(found.getUsername(), user.getUsername());
    assertEquals(found.getPassword(), user.getPassword());
    assertEquals(found.getEmail(), user.getEmail());
  }

  @Test
  public void getPictureStreamNullArg() {
    assertThrows(IllegalArgumentException.class, () -> userService.getPictureStream(null));
  }

  @Test
  public void getPictureStreamCommonCase() {
    userService.getPictureStream(user);
    verify(fileResourcesRepository).read(any(Path.class), eq(user.getPictureName()));
  }

  @Test
  public void updatePictureNullUser() {
    assertThrows(IllegalArgumentException.class,
        () -> userService.updatePicture(null, new byte[123]));
  }

  @Test
  public void updatePictureNullPicture() {
    assertThrows(IllegalArgumentException.class, () -> userService.updatePicture(user, null));
  }

  @Test
  public void updatePictureZeroSizePicture() {
    assertThrows(IllegalArgumentException.class,
        () -> userService.updatePicture(user, new byte[0]));
  }

  @Test
  public void updatePictureCommonCase() {
    assertEquals(User.getDefaultPictureName(), user.getPictureName());
    String newPicName = "newPicName";
    when(fileResourcesRepository.save(any(byte[].class), any(Path.class), anyString()))
        .thenReturn(newPicName);
    userService.updatePicture(user, new byte[1]);
    verify(fileResourcesRepository, times(0)).delete(any(Path.class), anyString());
    assertEquals(newPicName, user.getPictureName());
  }

  @Test
  public void changeCurrentUserDataNullUser() {
    UsernamePasswordEmailDto dto = new UsernamePasswordEmailDto(USERNAME, PASSWORD, EMAIL);
    assertThrows(IllegalArgumentException.class,
        () -> userService.changeCurrentUserData(null, dto));
  }

  @Test
  public void changeCurrentUserDataNullDto() {
    assertThrows(IllegalArgumentException.class,
        () -> userService.changeCurrentUserData(user, null));
  }

  @Test
  public void changeCurrentUserDataCommonCase() {
    UsernamePasswordEmailDto dto = new UsernamePasswordEmailDto("", NEW_PASSWORD, null);
    userService.changeCurrentUserData(user, dto);
    assertEquals(USERNAME, user.getUsername());
    assertTrue(passwordEncoder.matches(NEW_PASSWORD, user.getPassword()));
    assertEquals(EMAIL, user.getEmail());
  }

  @Test
  public void resetPasswordIdNotFound() {
    ResetPasswordDto dto = new ResetPasswordDto(0L, NEW_PASSWORD);
    assertThrows(NoSuchElementException.class, () -> userService.resetPassword(dto));
  }

  @Test
  public void resetPasswordWithoutToken() {
    assertNull(user.getResetPasswordToken());
    ResetPasswordDto dto = new ResetPasswordDto(user.getId(), NEW_PASSWORD);
    assertThrows(IllegalArgumentException.class, () -> userService.resetPassword(dto));
  }

  @Test
  public void resetPasswordCommonCase() {
    String oldPassword = user.getPassword();
    user.setResetPasswordToken(new ResetPasswordToken(user));
    entityManager.flush();
    ResetPasswordDto dto = new ResetPasswordDto(user.getId(), NEW_PASSWORD);
    userService.resetPassword(dto);
    assertNull(user.getResetPasswordToken());
    assertNotEquals(oldPassword, user.getPassword());
  }

  @Test
  public void sendResetPasswordEmailUsernameNotFound() {
    assertThrows(UsernameNotFoundException.class,
        () -> userService.sendResetPasswordEmail("gibberish"));
  }

  @Test
  public void sendResetPasswordEmailCommonCase() {
    userService.sendResetPasswordEmail(user.getUsername());
    verify(mailSender).send(any(SimpleMailMessage.class));
  }
}
