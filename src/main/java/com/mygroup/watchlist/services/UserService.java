package com.mygroup.watchlist.services;

import com.mygroup.watchlist.entities.User;
import com.mygroup.watchlist.exceptions.UsernameAlreadyTakenException;
import com.mygroup.watchlist.forms.RegistrationForm;
import com.mygroup.watchlist.repositories.FileSystemRepository;
import com.mygroup.watchlist.repositories.UserRepository;
import com.mygroup.watchlist.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final String PROFILE_PICTURES_FOLDER =
      "src\\main\\resources\\images\\user\\profile-pictures";

  private final UserRepository userRepository;
  private final FileSystemRepository fileRepository;
  private final PasswordEncoder passwordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, FileSystemRepository fileRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.fileRepository = fileRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User register(RegistrationForm form) {
    if (userRepository.existsByUsername(form.getUsername())) {
      throw new UsernameAlreadyTakenException(form.getUsername());
    }
    User user = new User();
    user.setUsername(form.getUsername());
    user.setPassword(passwordEncoder.encode(form.getPassword()));
    user.setRole(Role.USER);
    return userRepository.save(user);
  }

}
