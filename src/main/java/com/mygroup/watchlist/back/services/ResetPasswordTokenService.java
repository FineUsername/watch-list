package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.entities.ResetPasswordToken;
import com.mygroup.watchlist.back.repositories.ResetPasswordTokenRepository;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResetPasswordTokenService {

  private final ResetPasswordTokenRepository resetPasswordTokenRepository;

  @Autowired
  public ResetPasswordTokenService(ResetPasswordTokenRepository resetPasswordTokenRepository) {
    this.resetPasswordTokenRepository = resetPasswordTokenRepository;
  }

  /**
   * Finds token by body
   * 
   * @param body
   * @return token with that body
   * @throws NoSuchElementException if token with that body wasn't found
   */
  public ResetPasswordToken findByBody(UUID body) {
    return resetPasswordTokenRepository.findByBody(body).orElseThrow();
  }

  /**
   * Deletes token
   * 
   * @param token to delete
   * @throws IllegalArgumentException if token is null
   */
  public void deleteToken(ResetPasswordToken token) {
    if (token == null) {
      throw new IllegalArgumentException();
    }
    resetPasswordTokenRepository.delete(token);
  }

}
