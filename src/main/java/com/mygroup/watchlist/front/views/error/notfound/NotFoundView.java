package com.mygroup.watchlist.front.views.error.notfound;

import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.ErrorParameter;
import com.vaadin.flow.router.HasErrorParameter;
import com.vaadin.flow.router.NotFoundException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("not-found")
@PageTitle("Not found")
@AnonymousAllowed
@StyleSheet("context://frontend/not-found-styles/not-found.css")
public class NotFoundView extends AbstractView implements HasErrorParameter<NotFoundException> {

  public NotFoundView() {
    removeAll();
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
  }

  private VerticalLayout setupForm() {
    H2 description = new H2("Sorry, page you are trying to access does not exist.");
    description.setClassName("description");
    VerticalLayout form = new VerticalLayout(description);
    form.getStyle().clear();
    form.setClassName("not-found-form");
    return form;
  }

  @Override
  public int setErrorParameter(BeforeEnterEvent event,
      ErrorParameter<NotFoundException> parameter) {
    parameter.getException().printStackTrace();
    return 404;
  }
}
