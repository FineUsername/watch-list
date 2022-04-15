package com.mygroup.watchlist.forms;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class RegistrationForm {

  @NotEmpty(message = "Name can not be empty")
  private String username;

  // TODO remove hardcoding
  @Size(min = 8, max = 100, message = "Password must be between 8 and 100 symbols")
  private String password;

  @NotEmpty(message = "Password can not be empty")
  private String confirmPassword;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;

  }

  @AssertTrue(message = "Passwords dont match")
  public boolean isPasswordsMatch() {
    return ((password == null) && (confirmPassword == null))
        || ((password != null) && password.equals(confirmPassword));
  }

}
