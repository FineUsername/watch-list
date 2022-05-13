package com.mygroup.watchlist.back.repositories;

import com.mygroup.watchlist.back.entities.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Returns optional with entity with the given username, or Optional.empty() if none found.
   * 
   * @param username
   * @return Optional with entity with the given username, or Optional.empty() if none found.
   */
  Optional<User> findByUsername(String username);

  /**
   * Returns whether an entity with the given username exists.
   * 
   * @param username
   * @return true if an entity with the given username exists, false otherwise.
   */
  boolean existsByUsername(String username);

  /**
   * Returns whether an entity with the given email exists.
   * 
   * @param email
   * @return true if an entity with the given email exists, false otherwise.
   */
  boolean existsByEmail(String email);
}
