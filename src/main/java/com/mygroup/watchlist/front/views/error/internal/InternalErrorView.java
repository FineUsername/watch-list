package com.mygroup.watchlist.front.views.error.internal;

import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("internal-error")
@PageTitle("Internal error")
@AnonymousAllowed
@StyleSheet("context://frontend/internal-error-styles/internal-error.css")
public class InternalErrorView extends AbstractView implements HasErrorParameter<RuntimeException> {

  public InternalErrorView() {
    removeAll();
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
  }

  @Override
  public int setErrorParameter(BeforeEnterEvent event, ErrorParameter<RuntimeException> parameter) {
    parameter.getCaughtException().printStackTrace();
    return 500;
  }

  private VerticalLayout setupForm() {
    H2 description = new H2("Sorry, internal error occured. Try again later.");
    description.setClassName("description");
    VerticalLayout form = new VerticalLayout(description);
    form.getStyle().clear();
    form.setClassName("internal-error-form");
    return form;
  }

}
