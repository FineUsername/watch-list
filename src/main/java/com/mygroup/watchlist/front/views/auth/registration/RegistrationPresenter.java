package com.mygroup.watchlist.front.views.auth.registration;

import com.mygroup.watchlist.back.services.SecurityService;
import com.mygroup.watchlist.dto.UsernamePasswordDto;
import com.mygroup.watchlist.dto.UsernamePasswordEmailDto;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class RegistrationPresenter {

  private final SecurityService securityService;

  @Autowired
  public RegistrationPresenter(SecurityService securityService) {
    this.securityService = securityService;
  }

  public void registerAndLogin(UsernamePasswordEmailDto registrationDto) {
    securityService.registerAsUser(registrationDto);
    securityService.login(
        new UsernamePasswordDto(registrationDto.getUsername(), registrationDto.getPassword()));
  }

}
