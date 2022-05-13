package com.mygroup.watchlist.front.views.auth.login;

import com.mygroup.watchlist.dto.UsernamePasswordDto;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.mygroup.watchlist.front.views.auth.forgotpassword.ForgotPasswordView;
import com.mygroup.watchlist.front.views.auth.registration.RegistrationView;
import com.mygroup.watchlist.front.views.profile.ProfileView;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.Shortcuts;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Route("login")
@PageTitle("Login")
@AnonymousAllowed
@StyleSheet("context://frontend/login-styles/login.css")
public class LoginView extends AbstractView {

  private final LoginPresenter presenter;
  private final Binder<UsernamePasswordDto> binder;
  private UsernamePasswordDto loginDto;
  private PasswordField password;
  private TextField username;

  @Autowired
  public LoginView(LoginPresenter presenter) {
    this.presenter = presenter;
    binder = new Binder<>();
    loginDto = new UsernamePasswordDto();
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
    bind();
  }

  private VerticalLayout setupForm() {
    username = new TextField("Username");
    username.setValueChangeMode(ValueChangeMode.EAGER);

    password = new PasswordField("Password");
    password.setValueChangeMode(ValueChangeMode.EAGER);

    H2 title = new H2("Login");
    title.setClassName("header");

    Button loginButton = new Button("Enter", event -> {
      try {
        binder.writeBean(loginDto);
        presenter.login(loginDto);
        UI.getCurrent().navigate(ProfileView.class);
      } catch (BadCredentialsException | UsernameNotFoundException e) {
        showNotification("Invalid username or password", 3000, Position.MIDDLE,
            NotificationVariant.LUMO_ERROR);
      } catch (ValidationException e) {
        // Do nothing here, let user retry
      }
    });
    Shortcuts.addShortcutListener(this, loginButton::click, Key.ENTER);
    loginButton.setClassName("blue-button");
    loginButton.addClassName("blue-buttons-login");

    Button registerButton =
        new Button("Register", event -> UI.getCurrent().navigate(RegistrationView.class));
    registerButton.setClassName("blue-button");
    registerButton.addClassName("blue-buttons-login");

    Button forgotPasswordButton =
        new Button("Forgot password", event -> UI.getCurrent().navigate(ForgotPasswordView.class));
    forgotPasswordButton.setClassName("blue-button");
    forgotPasswordButton.addClassName("blue-buttons-login");

    HorizontalLayout buttonsRow = new HorizontalLayout();
    buttonsRow.add(forgotPasswordButton, registerButton);

    VerticalLayout form = new VerticalLayout();
    form.add(title, username, password, loginButton, buttonsRow);
    form.getStyle().clear();
    form.setClassName("login-form");
    return form;
  }

  private void bind() {
    binder.setBean(loginDto);
    binder.forField(username).bind(UsernamePasswordDto::getUsername,
        UsernamePasswordDto::setUsername);
    binder.forField(password).bind(UsernamePasswordDto::getPassword,
        UsernamePasswordDto::setPassword);
  }

}
