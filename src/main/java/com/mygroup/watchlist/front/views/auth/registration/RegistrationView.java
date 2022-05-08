package com.mygroup.watchlist.front.views.auth.registration;

import com.mygroup.watchlist.dto.UsernamePasswordEmailDto;
import com.mygroup.watchlist.exceptions.EmailAlreadyPresentException;
import com.mygroup.watchlist.exceptions.UsernameAlreadyTakenException;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.validation.PasswordValidator;
import com.mygroup.watchlist.front.views.AbstractView;
import com.mygroup.watchlist.front.views.auth.login.LoginView;
import com.mygroup.watchlist.front.views.profile.ProfileView;
import com.mygroup.watchlist.front.validation.NotBlankStringValidator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;

@Route("register")
@PageTitle("Register")
@AnonymousAllowed
@StyleSheet("context://frontend/registration-styles/registration.css")
public class RegistrationView extends AbstractView {

  private final RegistrationPresenter presenter;
  private final Binder<UsernamePasswordEmailDto> binder;
  private UsernamePasswordEmailDto registrationDto;
  private PasswordField password1;
  private PasswordField password2;
  private TextField username;
  private EmailField email;

  @Autowired
  public RegistrationView(RegistrationPresenter presenter) {
    this.presenter = presenter;
    binder = new Binder<>();
    registrationDto = new UsernamePasswordEmailDto();
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
    bind();
  }

  private VerticalLayout setupForm() {
    username = new TextField("Username");

    email = new EmailField("Email", "name@domain.com");

    password1 = new PasswordField("Password");

    password2 = new PasswordField("Confirm password");

    H2 title = new H2("Registration");
    title.setClassName("header");

    Button registrateButton = new Button("Registrate", event -> {
      try {
        binder.writeBean(registrationDto);
        presenter.registerAndLogin(registrationDto);
        UI.getCurrent().navigate(ProfileView.class);
      } catch (EmailAlreadyPresentException | UsernameAlreadyTakenException e) {
        showNotification(e.getMessage(), 3000, Position.MIDDLE, NotificationVariant.LUMO_ERROR);
      } catch (ValidationException e) {
        // Do nothing here, binder will display validators messages near fields with errors.
        // User is supposed to read them and try again.
      }
    });
    registrateButton.setClassName("blue-button");
    registrateButton.addClassName("blue-buttons-registration");

    Button alreadyRegisteredButton =
        new Button("Already have an account", event -> UI.getCurrent().navigate(LoginView.class));
    alreadyRegisteredButton.setClassName("blue-button");
    alreadyRegisteredButton.addClassName("blue-buttons-registration");

    VerticalLayout form = new VerticalLayout(title, username, email, password1, password2,
        registrateButton, alreadyRegisteredButton);
    form.getStyle().clear();
    form.setClassName("registration-form");
    return form;
  }

  private void bind() {
    binder.setBean(registrationDto);
    binder.forField(username).withValidator(new NotBlankStringValidator("Username can't be empty"))
        .bind(UsernamePasswordEmailDto::getUsername, UsernamePasswordEmailDto::setUsername);
    binder.forField(password1).withValidator(new PasswordValidator(false))
        .bind(UsernamePasswordEmailDto::getPassword, UsernamePasswordEmailDto::setPassword);
    binder.forField(password2)
        .withValidator(password -> Objects.equals(password1.getValue(), password2.getValue()),
            "Passwords dont match")
        .bind(UsernamePasswordEmailDto::getPassword, UsernamePasswordEmailDto::setPassword);
    binder.forField(email).withValidator(new EmailValidator("Email isn't valid"))
        .bind(UsernamePasswordEmailDto::getEmail, UsernamePasswordEmailDto::setEmail);
  }
}
