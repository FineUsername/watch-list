package com.mygroup.watchlist.entities;

import com.mygroup.watchlist.security.Role;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {

  @Id
  @GeneratedValue
  private long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(nullable = false)
  private String profilePicturePath = "default-picture.png";

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private Role role;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "users_animes", joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "anime_id"))
  private Set<Anime> animes;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singleton(new SimpleGrantedAuthority(role.getPrefixedName()));
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getProfilePicturePath() {
    return profilePicturePath;
  }

  public void setProfilePicturePath(String picturePath) {
    this.profilePicturePath = picturePath;
  }

  public Set<Anime> getAnimes() {
    return animes;
  }

  public void setAnimes(Set<Anime> animes) {
    this.animes = animes;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

}
