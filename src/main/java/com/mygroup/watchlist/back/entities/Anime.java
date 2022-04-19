package com.mygroup.watchlist.back.entities;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table
public class Anime {

  @Id
  @GeneratedValue
  private long id;

  @Column(nullable = false, unique = true)
  private String title;

  @Column(nullable = false)
  private String description;

  @Column(nullable = false)
  private String previewPicturePath;

  @Column(nullable = false)
  private String mainPicturePath;

  @ManyToMany(mappedBy = "animes")
  private Set<User> users;

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

  public String getPreviewPicturePath() {
    return previewPicturePath;
  }

  public void setPreviewPicturePath(String previewPicturePath) {
    this.previewPicturePath = previewPicturePath;
  }

  public String getMainPicturePath() {
    return mainPicturePath;
  }

  public void setMainPicturePath(String mainPicturePath) {
    this.mainPicturePath = mainPicturePath;
  }

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

}
