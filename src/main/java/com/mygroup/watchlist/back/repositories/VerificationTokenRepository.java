package com.mygroup.watchlist.back.repositories;

import com.mygroup.watchlist.back.entities.VerificationToken;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

  /**
   * Returns optional with entity with the given body, or Optional.empty() if none found.
   * 
   * @param body
   * @return Optional with entity with the given body, or Optional.empty() if none found.
   */
  Optional<VerificationToken> findByBody(UUID body);

  /**
   * Returns whether an entity with the given body exists.
   * 
   * @param body
   * @return true if an entity with the given body exists, false otherwise.
   */
  boolean existsByBody(UUID body);

  /**
   * Deletes an entity with the given body.
   * 
   * @param body
   */
  @Transactional
  void deleteByBody(UUID body);
}
