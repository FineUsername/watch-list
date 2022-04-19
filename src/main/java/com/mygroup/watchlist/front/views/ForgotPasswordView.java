package com.mygroup.watchlist.front.views;

import com.mygroup.watchlist.front.presenters.ForgotPasswordPresenter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import org.springframework.beans.factory.annotation.Autowired;

@Route("forgot-password")
@PageTitle("Forgot password")
@AnonymousAllowed
public class ForgotPasswordView extends AbstractView {

  private final ForgotPasswordPresenter presenter;

  @Autowired
  public ForgotPasswordView(ForgotPasswordPresenter presenter) {
    this.presenter = presenter;
    // TODO implement view
  }
}
