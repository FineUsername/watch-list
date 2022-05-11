package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.config.EmailProperties;
import com.mygroup.watchlist.back.config.PasswordEncoderConfig;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Import({SecurityService.class, AnimeService.class, PasswordEncoderConfig.class, EmailService.class,
    EmailProperties.class, JavaMailSenderImpl.class})
public class AnimeServiceIntegrationTestsConfig {

}
