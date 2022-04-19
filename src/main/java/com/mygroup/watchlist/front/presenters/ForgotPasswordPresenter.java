package com.mygroup.watchlist.front.presenters;

import com.mygroup.watchlist.back.services.UserService;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ForgotPasswordPresenter {

  private final UserService userService;

  @Autowired
  public ForgotPasswordPresenter(UserService userService) {
    this.userService = userService;
  }
}
