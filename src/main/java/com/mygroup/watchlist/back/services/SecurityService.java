package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.entities.VerificationToken;
import com.mygroup.watchlist.back.repositories.UserRepository;
import com.mygroup.watchlist.back.security.Role;
import com.mygroup.watchlist.back.security.SecurityUtils;
import com.mygroup.watchlist.dto.UsernamePasswordDto;
import com.mygroup.watchlist.dto.UsernamePasswordEmailDto;
import com.mygroup.watchlist.exceptions.EmailAlreadyPresentException;
import com.mygroup.watchlist.exceptions.UnauthenticatedException;
import com.mygroup.watchlist.exceptions.UsernameAlreadyTakenException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final EmailService emailService;

  @Autowired
  public SecurityService(UserRepository userRepository, PasswordEncoder passwordEncoder,
      EmailService emailService) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.emailService = emailService;
  }

  /**
   * Checks credentials in given dto, creates and places corresponding authentication in security
   * context.
   * 
   * @param dto
   * @throws IllegalArgumentException if dto is null
   * @throws BadCredentialsException
   */
  public void login(UsernamePasswordDto dto) {
    if (dto == null) {
      throw new IllegalArgumentException();
    }
    UserDetails checkedUserDetails = checkCredentials(dto.getUsername(), dto.getPassword());
    authenticate(checkedUserDetails);
  }

  /**
   * Performs logout for a http session obtained from given request.
   * 
   * @param request from which a HTTP session is obtained (cannot be null)
   */
  public void logout(HttpServletRequest request) {
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(request, null, null);
  }

  /**
   * Creates new user with given credentials and user role, sends confirmation request to given
   * email. No validation is performed as it was supposed to be conducted earlier in vaadin view.
   * 
   * @param dto
   * @throws IllegalArgumentException if dto is null
   */
  public void registerAsUser(UsernamePasswordEmailDto dto) {
    register(dto, Role.USER, true);
  }

  /**
   * Creates new user with given credentials and admin role, sending confirmation request to given
   * email is omitted. No validation is performed as it was supposed to be conducted earlier in
   * vaadin view.
   * 
   * @param dto
   * @throws IllegalArgumentException if dto is null
   */
  public void registerAsAdmin(UsernamePasswordEmailDto dto) {
    register(dto, Role.ADMIN, false);
  }

  /**
   * Gets currently authenticated user.
   * 
   * @return currently authenticated user
   * @throws UnauthenticatedException if current user is not authenticated
   */
  public User getCurrentlyAuthenticatedUser() {
    return userRepository.findByUsername(SecurityUtils.getCurrentlyAuthenticatedUsername())
        .orElseThrow(() -> new UnauthenticatedException());
  }

  private void register(UsernamePasswordEmailDto dto, Role role, boolean sendConfirmationEmail) {
    if (dto == null) {
      throw new IllegalArgumentException();
    }
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
    user.setRole(role);
    if (sendConfirmationEmail) {
      user.setVerificationToken(new VerificationToken(user));
      emailService.sendConfirmRegistrationEmail(dto.getEmail(),
          user.getVerificationToken().getBody().toString());
    }
    userRepository.save(user);
  }

  private User checkCredentials(String username, String password) {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
    if (!passwordEncoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Invalid username or password");
    }
    return user;
  }

  private void authenticate(UserDetails userDetails) {
    Authentication authentication = new UsernamePasswordAuthenticationToken(
        userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(authentication);
  }
}
