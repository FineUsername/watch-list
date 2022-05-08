package com.mygroup.watchlist.front.views.auth.forgotpassword;

import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;

@Route("forgot-password")
@PageTitle("Forgot password")
@AnonymousAllowed
public class ForgotPasswordView extends AbstractView {

  private final ForgotPasswordPresenter presenter;

  @Autowired
  public ForgotPasswordView(ForgotPasswordPresenter presenter) {
    this.presenter = presenter;
    add(new MyHeader(), setupForm(), new MyFooter());
  }

  private VerticalLayout setupForm() {
    H3 prompt = new H3("Enter your username");
    TextField username = new TextField("Username");

    Button submit = new Button("Submit");
    VerticalLayout form = new VerticalLayout(prompt, username, submit);

    submit.addClickListener(click -> {
      try {
        presenter.sendResetPasswordEmail(username.getValue());
        form.removeAll();
        form.add(new H3("Reset password message was sent to the email connected to your account"));
      } catch (NoSuchElementException e) {
        showNotification("No account with this username", 4000, Position.MIDDLE,
            NotificationVariant.LUMO_ERROR);
      }
    });
    return form;
  }
}
