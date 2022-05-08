package com.mygroup.watchlist.back.entities;

import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.PreRemove;
import javax.persistence.Table;

@Entity
@Table(name = "verification_token")
public class VerificationToken {

  @Id
  @GeneratedValue
  private long id;

  @Column(name = "body", nullable = false, unique = true, columnDefinition = "VARCHAR(36)")
  private UUID body = UUID.randomUUID();

  @OneToOne
  @JoinColumn(name = "user_id")
  private User user;

  public VerificationToken() {}

  public VerificationToken(User user) {
    this.user = user;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public UUID getBody() {
    return body;
  }

  public void setBody(UUID body) {
    this.body = body;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @PreRemove
  private void preRemove() {
    user.setVerificationToken(null);
  }

}
