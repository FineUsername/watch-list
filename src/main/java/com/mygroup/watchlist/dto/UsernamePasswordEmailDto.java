package com.mygroup.watchlist.dto;

public class UsernamePasswordEmailDto extends UsernamePasswordDto {

  private String email;

  public UsernamePasswordEmailDto() {}

  public UsernamePasswordEmailDto(String username, String password, String email) {
    super(username, password);
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
