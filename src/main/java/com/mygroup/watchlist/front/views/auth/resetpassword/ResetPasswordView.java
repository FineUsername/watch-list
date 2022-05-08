package com.mygroup.watchlist.front.views.auth.resetpassword;

import com.mygroup.watchlist.dto.ResetPasswordDto;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.validation.PasswordValidator;
import com.mygroup.watchlist.front.views.AbstractView;
import com.mygroup.watchlist.front.views.auth.login.LoginView;
import com.mygroup.watchlist.front.views.error.notfound.NotFoundView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;

@Route("reset-password")
@PageTitle("Reset password")
@AnonymousAllowed
public class ResetPasswordView extends AbstractView implements HasUrlParameter<String> {

  private final ResetPasswordPresenter presenter;
  private final Binder<ResetPasswordDto> binder = new Binder<>();
  private ResetPasswordDto dto = new ResetPasswordDto();
  private PasswordField password;

  @Autowired
  public ResetPasswordView(ResetPasswordPresenter presenter) {
    this.presenter = presenter;
    add(new MyHeader(), setupForm(), new MyFooter());
    bind();
  }

  @Override
  public void setParameter(BeforeEvent event, String parameter) {
    try {
      dto.setUserId(presenter.findTokenByBody(parameter).getUser().getId());
    } catch (NoSuchElementException e) {
      UI.getCurrent().navigate(NotFoundView.class);
    }
  }

  private VerticalLayout setupForm() {
    password = new PasswordField("New password");

    Button resetPassword = new Button("Reset password", click -> {
      try {
        binder.writeBean(dto);
        presenter.resetPassword(dto);
        UI.getCurrent().navigate(LoginView.class);
      } catch (ValidationException e) {

      }
    });

    VerticalLayout form = new VerticalLayout(password, resetPassword);
    return form;
  }

  private void bind() {
    binder.setBean(dto);
    binder.forField(password).withValidator(new PasswordValidator(false))
        .bind(ResetPasswordDto::getNewPassword, ResetPasswordDto::setNewPassword);
  }
}
