package com.mygroup.watchlist.back.config;

import com.mygroup.watchlist.back.services.SecurityService;
import com.mygroup.watchlist.back.services.UserService;
import com.mygroup.watchlist.dto.UsernamePasswordEmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdminConfig {

  private final UserService userService;
  private final SecurityService securityService;

  @Autowired
  public AdminConfig(UserService userService, SecurityService securityService) {
    this.userService = userService;
    this.securityService = securityService;
  }

  @Bean
  public CommandLineRunner initAdmin() {
    return (String... args) -> {
      String adminEmail = System.getenv("MAIL_USERNAME");
      if (userService.emailAlreadyTaken(adminEmail)) {
        return;
      }
      UsernamePasswordEmailDto adminDto = new UsernamePasswordEmailDto();
      adminDto.setUsername(System.getenv("ADMIN_USERNAME"));
      adminDto.setPassword(System.getenv("ADMIN_PASSWORD"));
      adminDto.setEmail(adminEmail);
      securityService.registerAsAdmin(adminDto);
    };
  }
}
