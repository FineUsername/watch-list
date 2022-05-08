package com.mygroup.watchlist.dto;

public class ResetPasswordDto {

  private long userId;
  private String newPassword;

  public ResetPasswordDto() {}

  public ResetPasswordDto(long userId, String newPassword) {
    this.userId = userId;
    this.newPassword = newPassword;
  }

  public long getUserId() {
    return userId;
  }

  public void setUserId(long userId) {
    this.userId = userId;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
}
