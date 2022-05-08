package com.mygroup.watchlist.services;

import com.mygroup.watchlist.back.config.EmailProperties;
import com.mygroup.watchlist.back.config.PasswordEncoderConfig;
import com.mygroup.watchlist.back.config.PicturesProperties;
import com.mygroup.watchlist.back.services.EmailService;
import com.mygroup.watchlist.back.services.ResetPasswordTokenService;
import com.mygroup.watchlist.back.services.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import({PasswordEncoderConfig.class, EmailProperties.class, UserService.class,
    ResetPasswordTokenService.class, EmailService.class, PicturesProperties.class})
@Configuration
public class UserServiceTestsConfig {

}
