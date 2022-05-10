package com.mygroup.watchlist.front.views.anime.browse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.security.SecurityUtils;
import com.mygroup.watchlist.back.services.AnimeService;
import com.mygroup.watchlist.dto.AnimePreviewDto;
import com.vaadin.flow.spring.annotation.SpringComponent;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class BrowseAnimePresenter {

  private static final int MAX_PREVIEWS_PER_PAGE = 10;

  private final AnimeService animeService;
  private Pageable currentPage;
  private Slice<AnimePreviewDto> currentSlice;
  private Set<WatchStatus> displayedStatuses;

  @Autowired
  public BrowseAnimePresenter(AnimeService animeService) {
    this.animeService = animeService;
    displayedStatuses = new HashSet<>();
    currentPage = Pageable.ofSize(MAX_PREVIEWS_PER_PAGE);
  }

  public void setStatusDisplayable(WatchStatus status, boolean displayable) {
    if (displayable) {
      displayedStatuses.add(status);
    } else {
      displayedStatuses.remove(status);
    }
  }

  public static int getMaxPreviewsPerPage() {
    return MAX_PREVIEWS_PER_PAGE;
  }

  public List<AnimePreviewDto> getAllPreviews() {
    return searchByTitle("");
  }

  public List<AnimePreviewDto> searchByTitle(String titlePart) {
    if (SecurityUtils.isUserAuthenticated()) {
      if (displayedStatuses.isEmpty()) {
        currentSlice = animeService.searchByTitleForCurrentUser(titlePart, currentPage);
      } else {
        currentSlice = animeService.searchByTitleForCurrentUserWithStatuses(titlePart,
            displayedStatuses, currentPage);
      }
    } else {
      currentSlice = animeService.searchByTitle(titlePart, currentPage);
    }
    return currentSlice.toList();
  }

  public void incrementPage() {
    currentPage = currentPage.next();
  }

  public void decrementPage() {
    currentPage = currentPage.previousOrFirst();
  }

  public boolean hasPreviousPage() {
    return currentSlice.hasPrevious();
  }

  public boolean hasNextPage() {
    return currentSlice.hasNext();
  }

}
