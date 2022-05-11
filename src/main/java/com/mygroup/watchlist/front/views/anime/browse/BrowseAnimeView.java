package com.mygroup.watchlist.front.views.anime.browse;

import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.security.SecurityUtils;
import com.mygroup.watchlist.front.components.AnimePreviewLayout;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;

@Route("anime/all")
@PageTitle("All")
@AnonymousAllowed
@StyleSheet("context://frontend/browse-styles/browse.css")
public class BrowseAnimeView extends AbstractView implements BeforeEnterObserver {

  private static final String PAGE_PARAM_NAME = "page";
  private static final String SEARCH_PARAM_NAME = "search";

  private final BrowseAnimePresenter presenter;
  private final VerticalLayout foundAnimeLayout = new VerticalLayout();
  private Map<WatchStatus, Checkbox> checboxStatus = new HashMap<>();
  private Button nextPageButton;
  private Button previousPageButton;

  @Autowired
  public BrowseAnimeView(BrowseAnimePresenter presenter) {
    this.presenter = presenter;
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
  }

  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    Map<String, List<String>> parameters = event.getLocation().getQueryParameters().getParameters();
    if (parameters.containsKey(PAGE_PARAM_NAME)) {
      presenter.setPage(Integer.parseInt(parameters.get(PAGE_PARAM_NAME).get(0)));
    }
    if (parameters.containsKey(SEARCH_PARAM_NAME)) {
      presenter.setSearchedTitlePart(parameters.get(SEARCH_PARAM_NAME).get(0));
    }
    for (WatchStatus status : WatchStatus.values()) {
      if (parameters.containsKey(status.getStringRepresentation())) {
        presenter.setStatusDisplayable(status,
            Boolean.parseBoolean(parameters.get(status.getStringRepresentation()).get(0)));
      }
    }
    initPreviewLayout();
    disableNavigationButtonsIfNeeded();
  }

  private VerticalLayout setupForm() {
    HorizontalLayout animesCheckboxesLayout = new HorizontalLayout(foundAnimeLayout);
    if (SecurityUtils.isUserAuthenticated()) {
      animesCheckboxesLayout.add(setupCheckboxesLayout());
    }
    animesCheckboxesLayout.getStyle().clear();
    animesCheckboxesLayout.setClassName("animes-checkboxes-layout");
    VerticalLayout layout = new VerticalLayout(setupSearchLayout(), animesCheckboxesLayout,
        setupNavigationalButtonsLayout());
    layout.getStyle().clear();
    layout.setClassName("browse-form");
    return layout;
  }

  private HorizontalLayout setupNavigationalButtonsLayout() {
    nextPageButton = new Button("Next page", click -> {
      UI.getCurrent().navigate("anime/all",
          createParameters(presenter.getPage() + 1, presenter.getSearchedTitlePart()));
    });
    nextPageButton.setClassName("blue-button");
    nextPageButton.addClassName("blue-button-browse");

    previousPageButton = new Button("Previous page", click -> {
      UI.getCurrent().navigate("anime/all",
          createParameters(presenter.getPage() - 1, presenter.getSearchedTitlePart()));
    });
    previousPageButton.setEnabled(false);
    previousPageButton.setClassName("blue-button");
    previousPageButton.addClassName("blue-button-browse");
    HorizontalLayout buttonsLayout = new HorizontalLayout(previousPageButton, nextPageButton);
    buttonsLayout.getStyle().clear();
    buttonsLayout.setClassName("buttons-layout");
    return buttonsLayout;
  }

  private HorizontalLayout setupSearchLayout() {
    TextField searchField = new TextField("", "Search for specific title");
    searchField.addClassName("search-field");
    searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
    Button searchButton = new Button("Search", click -> {
      if (presenter.getSearchedTitlePart().equals(searchField.getValue())) {
        return; // User tries to repeat the same search, no need to reload
      }
      UI.getCurrent().navigate("anime/all", createParameters(0, searchField.getValue()));
    });
    searchButton.setClassName("blue-button");
    searchButton.addClassName("blue-button-browse");
    HorizontalLayout searchLayout = new HorizontalLayout(searchField, searchButton);
    searchLayout.setClassName("search-layout");
    return searchLayout;
  }

  private VerticalLayout setupCheckboxesLayout() {
    H3 filterByStatus = new H3("Filter by status");
    filterByStatus.setClassName("browse-text");
    filterByStatus.addClassName("filter-header");
    VerticalLayout checkboxesLayout = new VerticalLayout(filterByStatus);
    checkboxesLayout.getStyle().clear();
    checkboxesLayout.setClassName("checkboxes");
    for (WatchStatus status : WatchStatus.values()) {
      Checkbox checkbox = new Checkbox(status.getStringRepresentation());
      checboxStatus.put(status, checkbox);
      checkbox.addValueChangeListener(event -> {
        UI.getCurrent().navigate("anime/all",
            createParameters(0, presenter.getSearchedTitlePart()));
      });
      checkboxesLayout.add(checkbox);
    }
    return checkboxesLayout;
  }

  private void disableNavigationButtonsIfNeeded() {
    nextPageButton.setEnabled(presenter.hasNextPage());
    previousPageButton.setEnabled(presenter.hasPreviousPage());
  }

  private void initPreviewLayout() {
    foundAnimeLayout.removeAll();
    foundAnimeLayout.add(presenter.getPreviews().map(dto -> new AnimePreviewLayout(dto)).toList()
        .toArray(new AnimePreviewLayout[0]));
    foundAnimeLayout.getStyle().clear();
    foundAnimeLayout.setClassName("found-anime-layout");
  }

  private QueryParameters createParameters(int page, String searchedTitlePart) {
    Map<String, String> params = new HashMap<>();
    params.put(PAGE_PARAM_NAME, String.valueOf(page));
    params.put(SEARCH_PARAM_NAME, searchedTitlePart);
    checboxStatus.forEach((status, checkbox) -> params.put(status.getStringRepresentation(),
        checkbox.getValue().toString()));
    return QueryParameters.simple(params);
  }

}
