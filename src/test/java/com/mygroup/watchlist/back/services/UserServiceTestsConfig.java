package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.config.EmailProperties;
import com.mygroup.watchlist.back.config.PasswordEncoderConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({PasswordEncoderConfig.class, EmailProperties.class, UserService.class,
    ResetPasswordTokenService.class, EmailService.class, SecurityService.class})
@Configuration
public class UserServiceTestsConfig {

}
