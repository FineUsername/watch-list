package com.mygroup.watchlist.back.repositories;

import com.mygroup.watchlist.back.entities.ResetPasswordToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResetPasswordTokenRepository extends JpaRepository<ResetPasswordToken, Long> {

  Optional<ResetPasswordToken> findByBody(UUID body);

}
