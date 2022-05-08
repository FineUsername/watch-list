package com.mygroup.watchlist.front.views.anime.add;

import com.mygroup.watchlist.dto.AddAnimeDto;
import com.mygroup.watchlist.exceptions.TitleAlreadyPresentException;
import com.mygroup.watchlist.front.components.BasicUpload;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.validation.NotBlankStringValidator;
import com.mygroup.watchlist.front.views.AbstractView;
import com.mygroup.watchlist.front.views.anime.AnimeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("anime/add")
@PageTitle("Upload")
@PermitAll
@StyleSheet("context://frontend/add-anime-styles/add-anime.css")
public class AddAnimeView extends AbstractView {

  private final AddAnimePresenter presenter;
  private final Binder<AddAnimeDto> binder;
  private AddAnimeDto dto;
  private TextField title;
  private TextArea description;
  private BasicUpload pictureUpload;

  @Autowired
  public AddAnimeView(AddAnimePresenter presenter) {
    this.presenter = presenter;
    binder = new Binder<>();
    dto = new AddAnimeDto();
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
    bind();
  }

  private VerticalLayout setupForm() {
    title = new TextField("Title");

    description = new TextArea("Description");

    pictureUpload = new BasicUpload("Picture");
    pictureUpload.setDropLabelIcon(new Label());
    pictureUpload.setUploadButton(new Button(new Icon(VaadinIcon.UPLOAD_ALT)));
    pictureUpload.setClassName("picture-upload");

    Button upload = new Button("Upload", event -> {
      try {
        binder.writeBean(dto);
        UI.getCurrent().navigate(AnimeView.class, String.valueOf(presenter.upload(dto).getId()));
      } catch (TitleAlreadyPresentException e) {
        showNotification(e.getMessage(), 3000, Position.MIDDLE, NotificationVariant.LUMO_CONTRAST);
      } catch (ValidationException e) {
        // let the user try again
      }
    });
    upload.setClassName("blue-button");
    upload.addClassName("blue-buttons-add-anime");
    VerticalLayout form = new VerticalLayout(title, description, pictureUpload, upload);
    form.getStyle().clear();
    form.setClassName("add-anime-form");
    return form;
  }

  private void bind() {
    binder.setBean(dto);
    binder.forField(title).withValidator(new NotBlankStringValidator("Title can't be empty"))
        .bind(AddAnimeDto::getTitle, AddAnimeDto::setTitle);
    binder.forField(description)
        .withValidator(new NotBlankStringValidator("Description can't be empty"))
        .bind(AddAnimeDto::getDescription, AddAnimeDto::setDescription);
    binder.forField(pictureUpload).withValidator(value -> value != null, "")
        .bind(AddAnimeDto::getPicture, AddAnimeDto::setPicture);
  }

}
