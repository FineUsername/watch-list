package com.mygroup.watchlist.front.views.anime.browse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.PageRequest;
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
  private Slice<AnimePreviewDto> currentSlice;
  private Set<WatchStatus> displayedStatuses;
  private String searchedTitlePart = "";
  private Pageable page = Pageable.ofSize(MAX_PREVIEWS_PER_PAGE);

  @Autowired
  public BrowseAnimePresenter(AnimeService animeService) {
    this.animeService = animeService;
    displayedStatuses = new HashSet<>();
  }

  public static int getMaxPreviewsPerPage() {
    return MAX_PREVIEWS_PER_PAGE;
  }

  public Slice<AnimePreviewDto> getPreviews() {
    if (!SecurityUtils.isUserAuthenticated()) {
      currentSlice = animeService.searchByTitle(searchedTitlePart, page);
      return currentSlice;
    }
    if (displayedStatuses.isEmpty()) {
      currentSlice = animeService.searchByTitleForCurrentUser(searchedTitlePart, page);
    } else {
      currentSlice = animeService.searchByTitleForCurrentUserWithStatuses(searchedTitlePart,
          displayedStatuses, page);
    }
    return currentSlice;
  }

  public void setPage(int page) {
    this.page = PageRequest.of(page, MAX_PREVIEWS_PER_PAGE);
  }

  public int getPage() {
    return page.getPageNumber();
  }

  public void setSearchedTitlePart(String searchedTitlePart) {
    this.searchedTitlePart = searchedTitlePart;
  }

  public String getSearchedTitlePart() {
    return searchedTitlePart;
  }

  public void setStatusDisplayable(WatchStatus status, boolean displayable) {
    if (displayable) {
      displayedStatuses.add(status);
    } else {
      displayedStatuses.remove(status);
    }
  }

  public boolean hasPreviousPage() {
    return currentSlice.hasPrevious();
  }

  public boolean hasNextPage() {
    return currentSlice.hasNext();
  }

}
