package com.mygroup.watchlist.back.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "user_anime")
public class UserAnime {

  public static enum WatchStatus {
    WATCHED, STOPPED, WANT_TO_WATCH;
  }

  @Id
  @GeneratedValue
  private long id;

  @ManyToOne
  private User user;

  @ManyToOne
  private Anime anime;

  @Enumerated(EnumType.ORDINAL)
  @Column
  private WatchStatus status;

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
