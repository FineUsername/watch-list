package com.mygroup.watchlist.front.presenters;

import com.mygroup.watchlist.back.services.AnimeService;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class WatchlistPresenter {

  private final AnimeService animeService;

  public WatchlistPresenter(AnimeService animeService) {
    this.animeService = animeService;
  }


}
