package com.mygroup.watchlist.front.views.profile;

import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.services.SecurityService;
import com.mygroup.watchlist.back.services.UserService;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProfilePresenter {

  private final UserService userService;
  private final SecurityService securityService;

  @Autowired
  public ProfilePresenter(UserService userService, SecurityService securityService) {
    this.userService = userService;
    this.securityService = securityService;
  }

  public User getCurrentUser() {
    return securityService.getCurrentlyAuthenticatedUser();
  }

  public User updateProfilePicture(User user, byte[] newPicture) {
    return userService.updatePicture(user, newPicture);
  }

  public StreamResource getProfilePictureStreamResource(User user) {
    return new StreamResource(user.getPictureName(), () -> userService.getPictureStream(user));
  }

  public void logout(HttpServletRequest request) {
    securityService.logout(request);
  }
}
