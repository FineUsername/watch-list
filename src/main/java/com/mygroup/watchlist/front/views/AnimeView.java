package com.mygroup.watchlist.front.views;

import com.mygroup.watchlist.back.entities.Anime;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.presenters.AnimePresenter;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;

@Route("anime")
@AnonymousAllowed
public class AnimeView extends AbstractView implements HasDynamicTitle, HasUrlParameter<Long> {

  private final AnimePresenter presenter;
  private Anime anime;

  @Autowired
  public AnimeView(AnimePresenter presenter) {
    this.presenter = presenter;
  }

  @Override
  public void setParameter(BeforeEvent event, Long id) {
    try {
      anime = presenter.getCurrentAnime(id);
    } catch (NoSuchElementException e) {
      // TODO handle no anime for that url, error page for not found 404 i guess
      UI.getCurrent().navigate("");
    }
    add(new MyHeader(), setupForm(), new MyFooter());
  }

  private VerticalLayout setupForm() {
    Image mainPicture = new Image(presenter.getMainPictureStreamResource(anime), "main picture");

    H2 title = new H2(anime.getTitle());

    H3 description = new H3(anime.getDescription());



    Button delete = new Button("Delete", event -> {
      presenter.deleteAnime(anime.getId());
      // TODO navigate to watchlist
    });
    if (!presenter.isAdminAuthenticated()) {
      delete.setVisible(false);
    }
    VerticalLayout form = new VerticalLayout(mainPicture, title, description, delete);
    return form;
  }

  @Override
  public String getPageTitle() {
    try {
      return anime.getTitle();
    } catch (NullPointerException e) {
      UI.getCurrent().navigate(""); // TODO error page
      return "";
    }
  }

}
