package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.entities.ResetPasswordToken;
import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.repositories.UserRepository;
import com.mygroup.watchlist.dto.UsernamePasswordEmailDto;
import com.mygroup.watchlist.dto.ResetPasswordDto;
import java.io.InputStream;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final ResetPasswordTokenService resetPasswordTokenService;
  private final EmailService emailService;

  @Autowired
  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      ResetPasswordTokenService resetPasswordTokenService, EmailService emailService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.resetPasswordTokenService = resetPasswordTokenService;
    this.emailService = emailService;
  }

  /**
   * Returns true if user with given email was found. Otherwise returns false.
   * 
   * @param email to look up
   * @return true if email was found, false otherwise
   */
  public boolean emailAlreadyTaken(String email) {
    return userRepository.existsByEmail(email);
  }

  /**
   * Finds user with given id.
   * 
   * @param id
   * @return user with given id
   * @throws NoSuchElementException if id wasn't found
   */
  public User findById(long id) {
    return userRepository.findById(id).orElseThrow();
  }

  /**
   * Finds user with given username. Username is unique - one username corresponds to only one user.
   * 
   * @param username
   * @return user with given username
   * @throws UsernameNotFoundException if username wasn't found
   */
  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(
        () -> new UsernameNotFoundException(String.format("Username %s not found", username)));
  }

  /**
   * Deletes old user's picture if it wasn't a default one, because default is shared between users.
   * Then sets given picture as user's picture.
   * 
   * @param user
   * @param newPicture
   * @return updated user for further operations
   * @throws FileOperationException if something goes wrong during file operations
   * @throws IllegalArgumentException if user is null, newPicture is null or newPicture's length is
   *         equal to 0
   */
  public User updatePicture(User user, byte[] newPicture) {
    if ((user == null) || (newPicture == null) || (newPicture.length == 0)) {
      throw new IllegalArgumentException();
    }
    user.setPicture(newPicture);
    return userRepository.save(user);
  }

  /**
   * Changes values of fields of a given user to corresponding not null and not empty values of a
   * given dto. If corresponding value in dto is empty, the field is left unchanged.
   * 
   * @param user
   * @param dto
   * @throws IllegalArgumentException if user is null or dto is null
   */
  public void changeCurrentUserData(User user, UsernamePasswordEmailDto dto) {
    if ((user == null) || (dto == null)) {
      throw new IllegalArgumentException();
    }
    changeUserData(dto, user);
    userRepository.save(user);
  }

  /**
   * Resets password for user with id given in dto. Also deletes ResetPasswordToken associated with
   * that user.
   * 
   * @param dto
   * @throws NoSuchElementException if user with given id wasn't found
   * @throws IllegalArgumentException if user with given id doesn't have a ResetPasswordToken
   *         instance associated with it
   * @see com.mygroup.watchlist.back.entities.ResetPasswordToken
   */
  public void resetPassword(ResetPasswordDto dto) {
    User user = findById(dto.getUserId());
    resetPasswordTokenService.deleteToken(user.getResetPasswordToken());
    user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
  }

  /**
   * Sends message with link to page where user can reset password. Email to send message to is
   * taken from user entity.
   * 
   * @param username of user to whose email send a message
   * @throws MailException if something goes wrong during email sending
   * @throws UsernameNotFound if argument wasn't found
   */
  public void sendResetPasswordEmail(String username) {
    User user = findByUsername(username);
    user.setResetPasswordToken(new ResetPasswordToken(user));
    emailService.sendResetPasswordEmail(user.getEmail(),
        user.getResetPasswordToken().getBody().toString());
  }

  private void changeUserData(UsernamePasswordEmailDto dto, User user) {
    if (StringUtils.hasText(dto.getEmail())) {
      user.setEmail(dto.getEmail());
    }
    if (StringUtils.hasText(dto.getUsername())) {
      user.setUsername(dto.getUsername());
    }
    if (StringUtils.hasText(dto.getPassword())) {
      user.setPassword(passwordEncoder.encode(dto.getPassword()));
    }
  }

}
