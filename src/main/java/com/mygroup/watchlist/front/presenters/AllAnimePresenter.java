package com.mygroup.watchlist.front.presenters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.back.services.AnimeService;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class AllAnimePresenter {
  private static final int MAX_PREVIEWS_PER_PAGE = 100;

  private final AnimeService animeService;

  private int pageNum = 1;

  @Autowired
  public AllAnimePresenter(AnimeService animeService) {
    this.animeService = animeService;
  }

  public static int getMaxPreviewsPerPage() {
    return MAX_PREVIEWS_PER_PAGE;
  }

  public List<Anime> getInitialAnimeList() {
    return animeService.findLimitedRange(MAX_PREVIEWS_PER_PAGE, getOffset());
  }

  public List<Anime> searchTitle(String titlePart) {
    return animeService.searchTitleLimitedRange(titlePart, MAX_PREVIEWS_PER_PAGE, getOffset());
  }

  public StreamResource getPreviewPictureStreamResource(Anime anime) {
    return new StreamResource(UUID.randomUUID().toString(), () -> {
      try {
        return animeService.getPreviewPicture(anime).getInputStream();
      } catch (IOException e) {
        // TODO handle
        throw new RuntimeException(e);
      }
    });
  }

  private int getOffset() {
    return MAX_PREVIEWS_PER_PAGE * (pageNum - 1);
  }
}
