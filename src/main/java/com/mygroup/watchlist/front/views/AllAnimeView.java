package com.mygroup.watchlist.front.views;

import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.front.components.AnimePreviewLayout;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.presenters.AllAnimePresenter;
import com.vaadin.flow.component.icon.VaadinIcon;
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
public class AllAnimeView extends AbstractView {

  private final AllAnimePresenter presenter;
  private final VerticalLayout foundAnimeLayout = new VerticalLayout();
  private final List<AnimePreviewLayout> previewLayoutsList =
      new ArrayList<>(AllAnimePresenter.getMaxPreviewsPerPage());

  @Autowired
  public AllAnimeView(AllAnimePresenter presenter) {
    this.presenter = presenter;
    add(new MyHeader(), setupForm(), new MyFooter());
  }

  private VerticalLayout setupForm() {
    // TODO implement pages
    presenter.getInitialAnimeList().forEach(anime -> previewLayoutsList
        .add(new AnimePreviewLayout(anime, presenter.getPreviewPictureStreamResource(anime))));
    foundAnimeLayout
        .add(previewLayoutsList.toArray(new AnimePreviewLayout[previewLayoutsList.size()]));

    TextField searchField = new TextField("", "Search for specific title");
    searchField.addValueChangeListener(
        event -> updateAnimeList(presenter.searchTitle(searchField.getValue())));
    searchField.setValueChangeMode(ValueChangeMode.EAGER);
    searchField.setPrefixComponent(VaadinIcon.SEARCH.create());

    VerticalLayout layout = new VerticalLayout(searchField, foundAnimeLayout);
    return layout;
  }

  private void updateAnimeList(List<Anime> animes) {
    Iterator<AnimePreviewLayout> iter = previewLayoutsList.iterator();
    animes.forEach(
        anime -> iter.next().changeAnime(anime, presenter.getPreviewPictureStreamResource(anime)));
    iter.forEachRemaining(layout -> layout.setVisible(false));
  }

}
