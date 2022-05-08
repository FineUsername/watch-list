package com.mygroup.watchlist.back.entities;

import com.mygroup.watchlist.back.security.Role;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "\"user\"")
public class User implements UserDetails {

  private static final String DEFAULT_PICTURE_NAME = "default-picture.jpg";

  @Id
  @GeneratedValue
  private long id;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false, name = "picture_name")
  private String pictureName = DEFAULT_PICTURE_NAME;

  @Column(nullable = false)
  @Enumerated(EnumType.ORDINAL)
  private Role role = Role.USER;

  @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @JoinColumn(name = "verification_token_id", unique = true)
  private VerificationToken verificationToken;

  @OneToOne(cascade = {CascadeType.REMOVE, CascadeType.PERSIST})
  @JoinColumn(name = "reset_password_token_id", unique = true)
  private ResetPasswordToken resetPasswordToken;

  @OneToMany(mappedBy = "user",
      cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
  private Set<UserAnimeRelation> userAnimeRelations;

  public User() {}

  public User(String username, String password, String email) {
    this.username = username;
    this.password = password;
    this.email = email;
  }

  public static String getDefaultPictureName() {
    return DEFAULT_PICTURE_NAME;
  }

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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPictureName() {
    return pictureName;
  }

  public void setPictureName(String pictureName) {
    this.pictureName = pictureName;
  }

  public Set<UserAnimeRelation> getUserAnimeRelations() {
    return userAnimeRelations;
  }

  public void setUserAnimeRelations(Set<UserAnimeRelation> userAnimeRelations) {
    this.userAnimeRelations = userAnimeRelations;
  }

  public VerificationToken getVerificationToken() {
    return verificationToken;
  }

  public void setVerificationToken(VerificationToken token) {
    this.verificationToken = token;
  }

  public ResetPasswordToken getResetPasswordToken() {
    return resetPasswordToken;
  }

  public void setResetPasswordToken(ResetPasswordToken resetPasswordToken) {
    this.resetPasswordToken = resetPasswordToken;
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
    return verificationToken == null;
  }

}
