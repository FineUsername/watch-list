package com.mygroup.watchlist.back.repositories;

import com.mygroup.watchlist.back.entities.Anime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AnimeRepository extends JpaRepository<Anime, Long> {

  Optional<Anime> findByTitle(String title);

  boolean existsByTitle(String title);

  @Query(value = "SELECT * FROM anime LIMIT ?1 OFFSET ?2", nativeQuery = true)
  List<Anime> findLimitedRange(long limit, long offset);

  @Query(value = "SELECT * FROM anime WHERE title LIKE %?1% LIMIT ?2 OFFSET ?3", nativeQuery = true)
  List<Anime> searchTitleLimitedRange(String titlePart, long limit, long offset);
}
