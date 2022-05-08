package com.mygroup.watchlist.back.services;

import com.mygroup.watchlist.back.repositories.VerificationTokenRepository;
import java.util.NoSuchElementException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenService {

  private final VerificationTokenRepository verificationTokenRepository;

  @Autowired
  public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
    this.verificationTokenRepository = verificationTokenRepository;
  }

  /**
   * Deletes token by its body
   * 
   * @param body
   * @throws NoSuchElementException if token with that body wasn't found
   */
  public void deleteToken(UUID body) {
    if (!verificationTokenRepository.existsByBody(body)) {
      throw new NoSuchElementException("No token with body: " + body);
    }
    verificationTokenRepository.deleteByBody(body);
  }
}
