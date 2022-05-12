package com.mygroup.watchlist.back.config;

import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class EmailProperties {

  private static final String CONFIRM_EMAIL_SUBJECT = "Confirm registration at watchlist";
  private static final String CONFIRM_EMAIL_TEXT =
      "Please, confirm your registration on watchlist by clicking on the link bellow\n\nhttps://watch-list123.herokuapp.com/confirm-registration/";
  private static final String RESET_PASSWORD_SUBJECT = "Reset password on watchlist";
  private static final String RESET_PASSWORD_TEXT =
      "Click on the link bellow to reset password for your account on watchlist\n\nhttps://watch-list123.herokuapp.com/reset-password/";

  public String getConfirmEmailSubject() {
    return CONFIRM_EMAIL_SUBJECT;
  }

  public String getConfirmEmailText() {
    return CONFIRM_EMAIL_TEXT;
  }

  public String getResetPasswordSubject() {
    return RESET_PASSWORD_SUBJECT;
  }

  public String getResetPasswordText() {
    return RESET_PASSWORD_TEXT;
  }
}
