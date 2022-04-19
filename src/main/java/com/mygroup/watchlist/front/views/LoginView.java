package com.mygroup.watchlist.front.views;

import com.mygroup.watchlist.dto.LoginDto;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.presenters.LoginPresenter;
import com.mygroup.watchlist.front.validation.NotBlankStringValidator;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
public class LoginView extends AbstractView {

  private final LoginPresenter presenter;
  private final Binder<LoginDto> binder;
  private LoginDto loginDto;
  private PasswordField password;
  private TextField username;

  @Autowired
  public LoginView(LoginPresenter presenter) {
    this.presenter = presenter;
    binder = new Binder<>();
    loginDto = new LoginDto();
    add(new MyHeader(), setupForm(), new MyFooter());
    bind();
  }

  private VerticalLayout setupForm() {
    username = new TextField("Username");

    password = new PasswordField("Password");

    H2 title = new H2("Login");

    Button loginButton = new Button("Enter", event -> {
      try {
        binder.writeBean(loginDto);
        presenter.login(loginDto);
        UI.getCurrent().navigate(ProfileView.class);
        // TODO navigate to tried view
      } catch (BadCredentialsException | UsernameNotFoundException e) {
        showNotification("Invalid username or password", 3000, Position.MIDDLE,
            NotificationVariant.LUMO_ERROR);
      } catch (ValidationException e) {
        // Do nothing here, binder will display validators' messages near fields with errors.
        // User is supposed to read them and try again.
      }
    });

    Button registrateButton =
        new Button("Registrate", event -> UI.getCurrent().navigate(RegistrationView.class));

    Button forgotPasswordButton =
        new Button("Forgot password", event -> UI.getCurrent().navigate(ForgotPasswordView.class));

    HorizontalLayout buttons = new HorizontalLayout();
    buttons.add(forgotPasswordButton, registrateButton);

    VerticalLayout form = new VerticalLayout();
    form.add(title, username, password, loginButton, buttons);
    return form;
  }

  private void bind() {
    binder.setBean(loginDto);
    binder.forField(username).withValidator(new NotBlankStringValidator("Username can't be empty"))
        .bind(LoginDto::getUsername, LoginDto::setUsername);
    binder.forField(password).withValidator(new NotBlankStringValidator("Password can't be empty"))
        .bind(LoginDto::getPassword, LoginDto::setPassword);
  }

}
