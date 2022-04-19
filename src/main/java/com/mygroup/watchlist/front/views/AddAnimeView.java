package com.mygroup.watchlist.front.views;

import com.mygroup.watchlist.dto.AddAnimeDto;
import com.mygroup.watchlist.exceptions.TitleAlreadyPresentException;
import com.mygroup.watchlist.front.components.BasicUpload;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.presenters.AddAnimePresenter;
import com.mygroup.watchlist.front.validation.NotBlankStringValidator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
public class AddAnimeView extends AbstractView {

  private final AddAnimePresenter presenter;
  private final Binder<AddAnimeDto> binder;
  private AddAnimeDto dto;
  private TextField title;
  private TextArea description;
  private BasicUpload previewPicture;
  private BasicUpload mainPicture;

  @Autowired
  public AddAnimeView(AddAnimePresenter presenter) {
    this.presenter = presenter;
    binder = new Binder<>();
    dto = new AddAnimeDto();
    add(new MyHeader(), setupForm(), new MyFooter());
    bind();
  }

  private VerticalLayout setupForm() {
    title = new TextField("Title");

    description = new TextArea("Description");

    previewPicture = new BasicUpload("Preview picture");

    mainPicture = new BasicUpload("Main picture");

    Button upload = new Button("Upload", event -> {
      try {
        binder.writeBean(dto);
        UI.getCurrent().navigate(AnimeView.class, presenter.upload(dto).getId());
      } catch (TitleAlreadyPresentException e) {
        showNotification(e.getMessage(), 3000, Position.MIDDLE, NotificationVariant.LUMO_CONTRAST);
      } catch (ValidationException e) {
        e.getBeanValidationErrors().forEach(a -> System.out.println(a));
        // let the user try again
      }
    });
    VerticalLayout form =
        new VerticalLayout(title, description, previewPicture, mainPicture, upload);
    return form;
  }

  private void bind() {
    binder.setBean(dto);
    binder.forField(title).withValidator(new NotBlankStringValidator("Title can't be empty"))
        .bind(AddAnimeDto::getTitle, AddAnimeDto::setTitle);
    binder.forField(description)
        .withValidator(new NotBlankStringValidator("Description can't be empty"))
        .bind(AddAnimeDto::getDescription, AddAnimeDto::setDescription);
    binder.forField(previewPicture).withValidator(value -> value != null, "")
        .bind(AddAnimeDto::getPreviewPicture, AddAnimeDto::setPreviewPicture);
    binder.forField(mainPicture).withValidator(value -> value != null, "")
        .bind(AddAnimeDto::getMainPicture, AddAnimeDto::setMainPicture);
    // TODO show messages for invalid pictures
  }

}
