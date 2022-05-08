package com.mygroup.watchlist.front.views.anime.add;

import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.services.AnimeService;
import com.mygroup.watchlist.dto.AddAnimeDto;
import com.vaadin.flow.spring.annotation.SpringComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AddAnimePresenter {

  private final AnimeService animeService;

  @Autowired
  public AddAnimePresenter(AnimeService animeService) {
    this.animeService = animeService;
  }

  public Anime upload(AddAnimeDto dto) {
    return animeService.add(dto);
  }
}
