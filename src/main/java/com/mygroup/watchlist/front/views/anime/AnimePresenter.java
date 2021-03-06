package com.mygroup.watchlist.front.views.anime;

import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.security.SecurityUtils;
import com.mygroup.watchlist.back.services.AnimeService;
import com.mygroup.watchlist.back.services.SecurityService;
import com.mygroup.watchlist.back.services.UserAnimeService;
import com.mygroup.watchlist.dto.AnimePreviewDto;
import com.mygroup.watchlist.dto.AnimeViewDto;
import com.mygroup.watchlist.exceptions.UnauthenticatedException;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AnimePresenter {

  private final AnimeService animeService;
  private final UserAnimeService userAnimeService;
  private final boolean isUserAuthenticated;
  private final long currentUserId;

  @Autowired
  public AnimePresenter(AnimeService animeService, UserAnimeService userAnimeService,
      SecurityService securityService) {
    this.animeService = animeService;
    this.userAnimeService = userAnimeService;
    isUserAuthenticated = SecurityUtils.isUserAuthenticated();
    if (isUserAuthenticated) {
      currentUserId = securityService.getCurrentlyAuthenticatedUser().getId();
    } else {
      currentUserId = 0;
    }
  }

  public AnimeViewDto getCurrentAnime(long id) {
    return animeService.getAnimeViewById(id);
  }

  public boolean isUserAuthenticated() {
    return isUserAuthenticated;
  }

  public WatchStatus getCurrentStatus(long animeId) {
    return userAnimeService.getStatus(currentUserId, animeId);
  }

  public void updateStatusForCurrentUser(WatchStatus status, long animeId) {
    userAnimeService.updateStatus(currentUserId, animeId, status);
  }

  public void deleteAnime(long id) {
    animeService.deleteById(id);
  }
}
