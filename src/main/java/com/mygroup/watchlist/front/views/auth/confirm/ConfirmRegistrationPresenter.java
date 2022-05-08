package com.mygroup.watchlist.front.views.auth.confirm;

import com.mygroup.watchlist.back.services.VerificationTokenService;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ConfirmRegistrationPresenter {

  private final VerificationTokenService verificationTokenService;

  @Autowired
  public ConfirmRegistrationPresenter(VerificationTokenService verificationTokenService) {
    this.verificationTokenService = verificationTokenService;
  }

  public void deleteUsedToken(String tokenBody) {
    verificationTokenService.deleteToken(UUID.fromString(tokenBody));
  }
}
