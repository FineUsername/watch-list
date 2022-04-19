package com.mygroup.watchlist.front.presenters;

import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.back.services.UserService;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ProfilePresenter {

  private final UserService userService;

  @Autowired
  public ProfilePresenter(UserService userService) {
    this.userService = userService;
  }

  public User getCurrentUser() {
    return userService.getCurrentlyAuthenticatedUser();
  }

  public User updateProfilePicture(User user, byte[] newPicture) {
    return userService.updateProfilePicture(user, newPicture);
  }

  public StreamResource getProfilePictureStreamResource(User user) {
    return new StreamResource(user.getProfilePicturePath(), () -> {
      try {
        return userService.getProfilePicture(user).getInputStream();
      } catch (IOException e) {
        // TODO handle
        throw new RuntimeException(e);
      }
    });
  }
}
