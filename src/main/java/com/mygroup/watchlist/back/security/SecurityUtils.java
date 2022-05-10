package com.mygroup.watchlist.back.security;

import com.mygroup.watchlist.exceptions.UnauthenticatedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

  public static boolean isAdminAuthenticated() {
    try {
      return getCurrentAuthentication().getAuthorities()
          .contains(new SimpleGrantedAuthority(Role.ADMIN.getPrefixedName()));
    } catch (UnauthenticatedException e) {
      return false;
    }
  }

  public static String getCurrentlyAuthenticatedUsername() {
    return getCurrentAuthentication().getName();
  }

  public static boolean isUserAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return isValidAuthentication(authentication);
  }

  private static Authentication getCurrentAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (!isValidAuthentication(authentication)) {
      throw new UnauthenticatedException();
    }
    return authentication;
  }

  private static boolean isValidAuthentication(Authentication authentication) {
    return (authentication != null) && !(authentication instanceof AnonymousAuthenticationToken);
  }
}
