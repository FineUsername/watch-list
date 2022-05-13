package com.mygroup.watchlist.back.repositories;

import com.mygroup.watchlist.back.entities.ResetPasswordToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

  /**
   * Returns optional with entity with the given body, or Optional.empty() if none found.
   * 
   * @param body
   * @return Optional with entity with the given body, or Optional.empty() if none found.
   */
  Optional<ResetPasswordToken> findByBody(UUID body);

}
