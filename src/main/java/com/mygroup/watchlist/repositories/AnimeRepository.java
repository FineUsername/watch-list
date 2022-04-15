package com.mygroup.watchlist.repositories;

import com.mygroup.watchlist.entities.Anime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

  Optional<Anime> findByTitle(String title);

  boolean existsByTitle(String title);

  @Query(value = "SELECT * FROM anime WHERE title LIKE %?%", nativeQuery = true)
  List<Anime> searchTitle(String string);
}
