package com.mygroup.watchlist.front.views.auth.confirm;

import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.mygroup.watchlist.front.views.error.notfound.NotFoundView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;

@Route("confirm-registration")
@AnonymousAllowed
@PageTitle("Registration confirmed")
@StyleSheet("context://frontend/confirm-styles/confirm.css")
public class ConfirmRegistrationView extends AbstractView implements HasUrlParameter<String> {

  private final ConfirmRegistrationPresenter presenter;

  @Autowired
  public ConfirmRegistrationView(ConfirmRegistrationPresenter presenter) {
    this.presenter = presenter;
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
  }

  private VerticalLayout setupForm() {
    H2 message = new H2("Registration successfully confirmed");
    message.setClassName("confirm-message");
    VerticalLayout form = new VerticalLayout(message);
    form.getStyle().clear();
    form.setClassName("confirm-form");
    return form;
  }

  @Override
  public void setParameter(BeforeEvent event, String parameter) {
    try {
      presenter.deleteUsedToken(parameter);
    } catch (NoSuchElementException e) {
      UI.getCurrent().navigate(NotFoundView.class);
    }
  }
}
