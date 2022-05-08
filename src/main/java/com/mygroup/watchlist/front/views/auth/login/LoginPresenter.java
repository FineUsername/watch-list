package com.mygroup.watchlist.front.views.auth.login;

import com.mygroup.watchlist.back.services.SecurityService;
import com.mygroup.watchlist.dto.UsernamePasswordDto;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LoginPresenter {

  private final SecurityService securityService;

  @Autowired
  public LoginPresenter(SecurityService securityService) {
    this.securityService = securityService;
  }

  public void login(UsernamePasswordDto loginDto) {
    securityService.login(loginDto);
  }

}
