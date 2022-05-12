package com.mygroup.watchlist.front.views.anime;

import com.mygroup.watchlist.back.entities.UserAnimeRelation.WatchStatus;
import com.mygroup.watchlist.back.security.SecurityUtils;
import com.mygroup.watchlist.dto.AnimeViewDto;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.mygroup.watchlist.front.views.auth.registration.RegistrationView;
import com.mygroup.watchlist.front.views.error.notfound.NotFoundView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.io.ByteArrayInputStream;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;

@Route("anime")
@AnonymousAllowed
@StyleSheet("context://frontend/anime-styles/anime.css")
public class AnimeView extends AbstractView implements HasDynamicTitle, HasUrlParameter<String> {

  private final AnimePresenter presenter;
  private AnimeViewDto dto;

  @Autowired
  public AnimeView(AnimePresenter presenter) {
    this.presenter = presenter;
    addClassName("centered-frame");
  }

  @Override
  public void setParameter(BeforeEvent event, String id) {
    try {
      dto = presenter.getCurrentAnime(Long.parseLong(id));
    } catch (NoSuchElementException e) {
      UI.getCurrent().navigate(NotFoundView.class);
      return;
    }
    add(new MyHeader(), setupForm(), new MyFooter());
  }

  @Override
  public String getPageTitle() {
    try {
      return dto.getTitle();
    } catch (NullPointerException e) {
      UI.getCurrent().navigate(NotFoundView.class);
      return "";
    }
  }

  private VerticalLayout setupForm() {
    Image picture = new Image(
        new StreamResource("", () -> new ByteArrayInputStream(dto.getPicture())), "main picture");
    picture.setClassName("anime-picture");
    H2 title = new H2(dto.getTitle());
    title.setClassName("text");
    Paragraph description = new Paragraph(dto.getDescription());
    description.setClassName("text");
    description.addClassName("description");
    VerticalLayout rightPart = new VerticalLayout();
    rightPart.getStyle().clear();
    rightPart.setClassName("right-part");
    if (presenter.isUserAuthenticated()) {
      ComboBox<WatchStatus> statusBox = setupComboBox();
      rightPart.add(statusBox);
    } else {
      H2 promptToRegister = new H2("Register to track");
      promptToRegister.setClassName("text");
      Button register =
          new Button("Register", click -> UI.getCurrent().navigate(RegistrationView.class));
      register.setClassName("blue-button");
      register.addClassName("blue-button-anime");
      rightPart.add(promptToRegister, register);
    }
    if (SecurityUtils.isAdminAuthenticated()) {
      Button delete = setupDeleteButton();
      rightPart.add(delete);
    }
    HorizontalLayout upperLayout = new HorizontalLayout(picture, rightPart);
    upperLayout.setClassName("upper-layout");
    VerticalLayout form = new VerticalLayout(upperLayout, title, description);
    form.getStyle().clear();
    form.setClassName("anime-form");
    return form;
  }

  private Button setupDeleteButton() {
    Button delete = new Button("Delete", event -> {
      presenter.deleteAnime(dto.getId());
      UI.getCurrent().getPage().getHistory().back();
    });
    delete.setClassName("blue-button");
    delete.addClassName("blue-button-anime");
    return delete;
  }

  private ComboBox<WatchStatus> setupComboBox() {
    ComboBox<WatchStatus> statusBox = new ComboBox<>("Status", WatchStatus.values());
    try {
      statusBox.setValue(presenter.getCurrentStatus(dto.getId()));
    } catch (NoSuchElementException e) {
      statusBox.setValue(statusBox.getEmptyValue());
    }
    statusBox.setItemLabelGenerator(WatchStatus::getStringRepresentation);
    statusBox.addValueChangeListener(
        event -> presenter.updateStatusForCurrentUser(statusBox.getValue(), dto.getId()));
    return statusBox;
  }
}
