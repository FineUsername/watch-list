package com.mygroup.watchlist.back.entities;

import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "anime")
public class Anime {

  @Id
  @GeneratedValue
  private long id;

  @Column(nullable = false, unique = true)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false, name = "picture")
  private byte[] picture;

  @OneToMany(mappedBy = "anime",
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
  private Set<UserAnimeRelation> userAnimeRelations;

  public Anime() {}

  public Anime(String title, String description, byte[] picture) {
    this.title = title;
    this.description = description;
    this.picture = picture;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public byte[] getPicture() {
    return picture;
  }

  public void setPicture(byte[] picture) {
    this.picture = picture;
  }

  public Set<UserAnimeRelation> getUserAnimeRelations() {
    return userAnimeRelations;
  }

  public void setUserAnimeRelations(Set<UserAnimeRelation> userAnimeRelations) {
    this.userAnimeRelations = userAnimeRelations;
  }

}
