package com.mygroup.watchlist.front.views.auth.resetpassword;

import com.mygroup.watchlist.back.entities.ResetPasswordToken;
import com.mygroup.watchlist.back.services.ResetPasswordTokenService;
import com.mygroup.watchlist.back.services.UserService;
import com.mygroup.watchlist.dto.ResetPasswordDto;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ResetPasswordPresenter {

  private final ResetPasswordTokenService resetPasswordTokenService;
  private final UserService userService;

  @Autowired
  public ResetPasswordPresenter(ResetPasswordTokenService resetPasswordTokenService,
      UserService userService) {
    this.resetPasswordTokenService = resetPasswordTokenService;
    this.userService = userService;
  }

  public ResetPasswordToken findTokenByBody(String body) {
    return resetPasswordTokenService.findByBody(UUID.fromString(body));
  }

  public void resetPassword(ResetPasswordDto dto) {
    userService.resetPassword(dto);
  }
}
