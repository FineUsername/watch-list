package com.mygroup.watchlist.services;

import com.mygroup.watchlist.back.config.EmailProperties;
import com.mygroup.watchlist.back.config.PasswordEncoderConfig;
import com.mygroup.watchlist.back.config.PicturesProperties;
import com.mygroup.watchlist.back.services.AnimeService;
import com.mygroup.watchlist.back.services.EmailService;
import com.mygroup.watchlist.back.services.SecurityService;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Import({SecurityService.class, AnimeService.class, PicturesProperties.class,
    PasswordEncoderConfig.class, EmailService.class, EmailProperties.class,
    JavaMailSenderImpl.class})
public class AnimeServiceIntegrationTestsConfig {

}
