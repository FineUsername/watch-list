package com.mygroup.watchlist.front.views.anime.browse;

import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.security.SecurityUtils;
import com.mygroup.watchlist.dto.AnimeViewDto;
import com.mygroup.watchlist.front.components.AnimePreviewLayout;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

@Route("anime/all")
@PageTitle("All")
@AnonymousAllowed
@StyleSheet("context://frontend/browse-styles/browse.css")
public class BrowseAnimeView extends AbstractView {

  private final BrowseAnimePresenter presenter;
  private final VerticalLayout foundAnimeLayout = new VerticalLayout();
  private final List<AnimePreviewLayout> previewLayoutsList =
      new ArrayList<>(BrowseAnimePresenter.getMaxPreviewsPerPage());
  private Button nextPageButton;
  private Button previousPageButton;
  private TextField searchField;

  @Autowired
  public BrowseAnimeView(BrowseAnimePresenter presenter) {
    this.presenter = presenter;
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
  }

  private VerticalLayout setupForm() {
    initPreviewLayout();
    setupSearchField();

    nextPageButton = new Button("Next page", click -> {
      presenter.incrementPage();
      updateAnimeList(presenter.searchByTitle(searchField.getValue()));
    });
    nextPageButton.setClassName("blue-button");
    nextPageButton.addClassName("blue-button-browse");

    previousPageButton = new Button("Previous page", click -> {
      presenter.decrementPage();
      updateAnimeList(presenter.searchByTitle(searchField.getValue()));
    });
    previousPageButton.setEnabled(false);
    previousPageButton.setClassName("blue-button");
    previousPageButton.addClassName("blue-button-browse");
    disableNavigationButtonsIfNeeded();

    HorizontalLayout animesCheckboxesLayout = new HorizontalLayout(foundAnimeLayout);
    if (SecurityUtils.isUserAuthenticated()) {
      animesCheckboxesLayout.add(setupCheckboxesLayout());
    }
    animesCheckboxesLayout.getStyle().clear();
    animesCheckboxesLayout.setClassName("animes-checkboxes-layout");
    HorizontalLayout buttonsLayout = new HorizontalLayout(previousPageButton, nextPageButton);
    buttonsLayout.getStyle().clear();
    buttonsLayout.setClassName("buttons-layout");
    VerticalLayout layout = new VerticalLayout(searchField, animesCheckboxesLayout, buttonsLayout);
    layout.getStyle().clear();
    layout.setClassName("browse-form");
    return layout;
  }

  private TextField setupSearchField() {
    searchField = new TextField("", "Search for specific title");
    searchField.addClassName("search-field");
    searchField.addValueChangeListener(
        event -> updateAnimeList(presenter.searchByTitle(searchField.getValue())));
    searchField.setValueChangeMode(ValueChangeMode.EAGER);
    searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
    return searchField;
  }

  private VerticalLayout setupCheckboxesLayout() {
    H3 filterByStatus = new H3("Filter by status");
    filterByStatus.setClassName("text");
    VerticalLayout checkboxesLayout = new VerticalLayout(filterByStatus);
    checkboxesLayout.getStyle().clear();
    checkboxesLayout.setClassName("checkboxes");
    for (WatchStatus status : WatchStatus.values()) {
      Checkbox checkbox = new Checkbox(status.getStringRepresentation());
      checkbox.addValueChangeListener(event -> {
        presenter.setStatusDisplayable(status, checkbox.getValue());
        updateAnimeList(presenter.searchByTitle(searchField.getValue()));
      });
      checkboxesLayout.add(checkbox);
    }
    return checkboxesLayout;
  }

  private void disableNavigationButtonsIfNeeded() {
    nextPageButton.setEnabled(presenter.hasNextPage());
    previousPageButton.setEnabled(presenter.hasPreviousPage());
  }

  private void updateAnimeList(List<AnimeViewDto> previews) {
    disableNavigationButtonsIfNeeded();
    Iterator<AnimePreviewLayout> iter = previewLayoutsList.iterator();
    previews.forEach(anime -> {
      AnimePreviewLayout layout = iter.next();
      layout.setVisible(false);
      layout.changeAnime(anime);
      layout.setVisible(true);
    });
    iter.forEachRemaining(layout -> layout.setVisible(false));
  }

  private void initPreviewLayout() {
    presenter.getAllPreviews().forEach(dto -> previewLayoutsList.add(new AnimePreviewLayout(dto)));
    foundAnimeLayout.add(previewLayoutsList.toArray(new AnimePreviewLayout[0]));
    foundAnimeLayout.getStyle().clear();
    foundAnimeLayout.setClassName("found-anime-layout");
  }

}
