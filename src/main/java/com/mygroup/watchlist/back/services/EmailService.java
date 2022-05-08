package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.config.EmailProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

  private final EmailProperties emailProperties;
  private final JavaMailSender mailSender;

  @Autowired
  public EmailService(EmailProperties emailProperties, JavaMailSender mailSender) {
    this.emailProperties = emailProperties;
    this.mailSender = mailSender;
  }

  public void sendConfirmRegistrationEmail(String destinationEmail, String link) {
    sendMail(destinationEmail, emailProperties.getConfirmEmailSubject(),
        emailProperties.getConfirmEmailText() + link);
  }

  public void sendResetPasswordEmail(String destinationEmail, String link) {
    sendMail(destinationEmail, emailProperties.getResetPasswordSubject(),
        emailProperties.getResetPasswordText() + link);
  }

  public void sendMail(String destinationEmail, String subject, String text) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setTo(destinationEmail);
    message.setSubject(subject);
    message.setText(text);
    mailSender.send(message);
  }

}
