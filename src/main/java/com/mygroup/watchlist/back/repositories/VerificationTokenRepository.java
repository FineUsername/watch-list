package com.mygroup.watchlist.back.repositories;

import com.mygroup.watchlist.back.entities.VerificationToken;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

  Optional<VerificationToken> findByBody(UUID body);

  boolean existsByBody(UUID body);

  @Transactional
  void deleteByBody(UUID body);
}
