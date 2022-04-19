package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.repositories.FileResourcesRepository;
import com.mygroup.watchlist.back.repositories.UserRepository;
import com.mygroup.watchlist.back.security.Role;
import com.mygroup.watchlist.dto.LoginDto;
import com.mygroup.watchlist.dto.RegistrationDto;
import com.mygroup.watchlist.exceptions.EmailAlreadyPresentException;
import com.mygroup.watchlist.exceptions.UnauthenticatedException;
import com.mygroup.watchlist.exceptions.UsernameAlreadyTakenException;
import com.vaadin.flow.server.InputStreamFactory;
import java.io.IOException;
import java.net.URL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final String PROFILE_PICTURES_FOLDER = "images/user/profile-picture";
  private static final String DEFAULT_PROFILE_PICTURE_PATH = "default-picture.jpg";
  private static final String EXTENSION = ".jpg"; // TODO store it somewhere in one place

  private final UserRepository userRepository;
  private final UserDetailsService userDetailsService;
  private final FileResourcesRepository fileResourcesRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, UserDetailsService userDetailsService,
      FileResourcesRepository fileResourcesRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.userDetailsService = userDetailsService;
    this.fileResourcesRepository = fileResourcesRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User register(RegistrationDto dto) {
    if (userRepository.existsByEmail(dto.getEmail())) {
      throw new EmailAlreadyPresentException(dto.getEmail());
    }
    if (userRepository.existsByUsername(dto.getUsername())) {
      throw new UsernameAlreadyTakenException(dto.getUsername());
    }
    User user = new User();
    user.setUsername(dto.getUsername());
    user.setEmail(dto.getEmail());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setRole(Role.USER);
    return userRepository.save(user);
  }

  public User registerWithAuthentication(RegistrationDto dto) {
    User user = register(dto);
    login(dto.getUsername(), dto.getPassword());
    return user;
  }

  public void login(LoginDto dto) {
    login(dto.getUsername(), dto.getPassword());
  }

  public User findByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(
        () -> new UsernameNotFoundException(String.format("Username %s not found", username)));
  }

  public User getCurrentlyAuthenticatedUser() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication instanceof AnonymousAuthenticationToken) {
      throw new UnauthenticatedException();
    }
    return findByUsername(authentication.getName());
  }

  public FileSystemResource getProfilePicture(User user) {
    return fileResourcesRepository.find(getProfilePictureUrl(user));
  }

  public User updateProfilePicture(User user, byte[] newPicture) {
    if (!user.getProfilePicturePath().equals(DEFAULT_PROFILE_PICTURE_PATH)) {
      deleteProfilePicture(user);
    }
    user.setProfilePicturePath(
        fileResourcesRepository.save(newPicture, PROFILE_PICTURES_FOLDER, EXTENSION));
    return userRepository.save(user);
  }

  public void deleteProfilePicture(User user) {
    fileResourcesRepository.delete(getProfilePictureUrl(user));
  }

  private String getProfilePictureUrl(User user) {
    return PROFILE_PICTURES_FOLDER + '/' + user.getProfilePicturePath();
  }

  private void login(String username, String password) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(username);
    if (!passwordEncoder.matches(password, userDetails.getPassword())) {
      throw new BadCredentialsException("Invalid username or password");
    }
    authenticate(userDetails);
  }

  private void authenticate(UserDetails userDetails) {
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }

}
