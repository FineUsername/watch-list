package com.mygroup.watchlist.front.views.about;

import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("about-site")
@PageTitle("About site")
@AnonymousAllowed
@StyleSheet("context://frontend/about-site-styles/about-site.css")
public class AboutSiteView extends AbstractView {

  public AboutSiteView() {
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
  }

  private VerticalLayout setupForm() {
    H3 description = new H3(
        "This site is a pet-project for a backend developer (who also had to suffer designing and writing front end");
    description.setClassName("description");
    H4 promptToContact = new H4("You can contact developer by:");
    promptToContact.setClassName("common-text");
    H4 email = new H4("email: prudnikovdma@gmail.com");
    email.setClassName("common-text");
    H4 telegramUsername = new H4("telegram: @kotfrh");
    telegramUsername.setClassName("common-text");
    VerticalLayout form = new VerticalLayout(description, promptToContact, email, telegramUsername);
    form.getStyle().clear();
    form.setClassName("about-site-form");
    return form;
  }
}
