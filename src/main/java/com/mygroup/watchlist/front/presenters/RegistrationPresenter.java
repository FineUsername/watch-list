package com.mygroup.watchlist.front.presenters;

import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.services.UserService;
import com.mygroup.watchlist.dto.RegistrationDto;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistrationPresenter {

  private final UserService userService;

  @Autowired
  public RegistrationPresenter(UserService userService) {
    this.userService = userService;
  }

  public User registerWithAuthentication(RegistrationDto registrationDto) {
    return userService.registerWithAuthentication(registrationDto);
  }

}
