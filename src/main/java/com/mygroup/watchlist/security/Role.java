package com.mygroup.watchlist.security;

public enum Role {
  USER("ROLE_USER"), ADMIN("ROLE_ADMIN");

  private final String prefixedName;

  private Role(String prefixedName) {
    this.prefixedName = prefixedName;
  }

  public String getPrefixedName() {
    return prefixedName;
  }
}
