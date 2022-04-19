package com.mygroup.watchlist.front.presenters;

import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.security.Role;
import com.mygroup.watchlist.back.services.AnimeService;
import com.mygroup.watchlist.back.services.UserService;
import com.mygroup.watchlist.exceptions.UnauthenticatedException;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.io.IOException;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnimePresenter {

  private final AnimeService animeService;
  private final UserService userService;

  @Autowired
  public AnimePresenter(AnimeService animeService, UserService userService) {
    this.animeService = animeService;
    this.userService = userService;
  }

  public Anime getCurrentAnime(long id) {
    return animeService.findById(id);
  }

  public StreamResource getMainPictureStreamResource(Anime anime) {
    return new StreamResource(UUID.randomUUID().toString(), () -> {
      try {
        return animeService.getMainPicture(anime).getInputStream();
      } catch (IOException e) {
        // TODO handle
        throw new RuntimeException(e);
      }
    });
  }

  public boolean isAdminAuthenticated() {
    try {
      return userService.getCurrentlyAuthenticatedUser().getAuthorities()
          .contains(new SimpleGrantedAuthority(Role.ADMIN.getPrefixedName()));
    } catch (UnauthenticatedException e) {
      return false;
    }
  }

  public void deleteAnime(long id) {
    animeService.deleteById(id);
  }
}
