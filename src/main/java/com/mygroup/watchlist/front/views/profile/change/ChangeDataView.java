package com.mygroup.watchlist.front.views.profile.change;

import com.mygroup.watchlist.dto.UsernamePasswordEmailDto;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.validation.PasswordValidator;
import com.mygroup.watchlist.front.views.AbstractView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("profile/change-data")
@PageTitle("Change data")
@PermitAll
@StyleSheet("context://frontend/change-personal-data-styles/change-personal-data.css")
public class ChangeDataView extends AbstractView {

  private final ChangeDataPresenter presenter;
  private final Binder<UsernamePasswordEmailDto> binder = new Binder<>();
  private UsernamePasswordEmailDto dto;
  private TextField username;
  private EmailField email;
  private PasswordField password;
  private Button saveChanges;

  @Autowired
  public ChangeDataView(ChangeDataPresenter presenter) {
    this.presenter = presenter;
    dto = new UsernamePasswordEmailDto();
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
    bind();
  }

  private VerticalLayout setupForm() {
    username = new TextField("Change username", click -> setSaveChangesEnabled());

    email = new EmailField("Change email", click -> setSaveChangesEnabled());

    password = new PasswordField("Change password", click -> setSaveChangesEnabled());

    saveChanges = new Button("Save changes", click -> {
      try {
        binder.writeBean(dto);
        presenter.changeUserData(dto);
        showNotification("Change successfull", 3000, Position.MIDDLE,
            NotificationVariant.LUMO_SUCCESS);
      } catch (ValidationException e) {
        // Do nothing, binder will display necessary prompts on its own
      }
    });
    saveChanges.setClassName("blue-button");
    saveChanges.addClassName("blue-button-change-personal-data");
    setSaveChangesEnabled();

    VerticalLayout form = new VerticalLayout(username, email, password, saveChanges);
    form.getStyle().clear();
    form.setClassName("change-personal-data-form");
    return form;
  }

  private void bind() {
    binder.setBean(dto);
    binder.forField(username).bind(UsernamePasswordEmailDto::getUsername,
        UsernamePasswordEmailDto::setUsername);
    binder.forField(password).withValidator(new PasswordValidator(true))
        .bind(UsernamePasswordEmailDto::getPassword, UsernamePasswordEmailDto::setPassword);
    binder.forField(email).withValidator(new EmailValidator("Email isn't valid", true))
        .bind(UsernamePasswordEmailDto::getEmail, UsernamePasswordEmailDto::setEmail);
  }

  private void setSaveChangesEnabled() {
    saveChanges.setEnabled(
        (!username.isEmpty() || !email.isEmpty() || !password.isEmpty()) && binder.isValid());
  }
}
