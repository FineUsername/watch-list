package com.mygroup.watchlist.back.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "user_anime_relation",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "anime_id"}))
public class UserAnimeRelation {

  public static enum WatchStatus {
    WATCHED("Watched"), STOPPED("Stopped"), PLANNED("Planned");

    private String stringRepresentation;

    private WatchStatus(String stringRepresentation) {
      this.stringRepresentation = stringRepresentation;
    }

    public String getStringRepresentation() {
      return stringRepresentation;
    }
  }

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
  @JoinColumn(name = "anime_id", nullable = false)
  private Anime anime;

  @Enumerated(EnumType.ORDINAL)
  @Column
  private WatchStatus status;

  public UserAnimeRelation() {}

  public UserAnimeRelation(User user, Anime anime, WatchStatus status) {
    this.user = user;
    this.anime = anime;
    this.status = status;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Anime getAnime() {
    return anime;
  }

  public void setAnime(Anime anime) {
    this.anime = anime;
  }

  public WatchStatus getStatus() {
    return status;
  }

  public void setStatus(WatchStatus status) {
    this.status = status;
  }

}
