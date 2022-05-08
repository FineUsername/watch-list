package com.mygroup.watchlist.front.views.profile.change;

import com.mygroup.watchlist.back.services.SecurityService;
import com.mygroup.watchlist.back.services.UserService;
import com.mygroup.watchlist.dto.UsernamePasswordEmailDto;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ChangeDataPresenter {

  private final UserService userService;
  private final SecurityService securityService;

  @Autowired
  public ChangeDataPresenter(UserService userService, SecurityService securityService) {
    this.userService = userService;
    this.securityService = securityService;
  }

  public void changeUserData(UsernamePasswordEmailDto dto) {
    userService.changeCurrentUserData(securityService.getCurrentlyAuthenticatedUser(), dto);
  }
}
