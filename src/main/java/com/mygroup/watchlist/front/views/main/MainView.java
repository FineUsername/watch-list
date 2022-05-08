package com.mygroup.watchlist.front.views.main;

import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.mygroup.watchlist.front.views.auth.registration.RegistrationView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("")
@AnonymousAllowed
@PageTitle("Watchlist")
@StyleSheet("context://frontend/main-styles/main.css")
public class MainView extends AbstractView {

  public MainView() {
    add(new MyHeader(), setupForm(), new MyFooter());
    addClassName("centered-frame");
  }

  private VerticalLayout setupForm() {

    H1 title = new H1("Welcome to anime watchlist");
    title.addClassName("header");

    Label introduction =
        new Label("Here you can track you progress by setting different statuses to titles:");
    introduction.addClassName("subheader");

    Button registrateButton =
        new Button("Join!", click -> UI.getCurrent().navigate(RegistrationView.class));
    registrateButton.setClassName("blue-button");
    registrateButton.addClassName("blue-button-join");

    VerticalLayout form =
        new VerticalLayout(title, introduction, setupStatusesDescription(), registrateButton);
    form.getStyle().clear();
    form.setClassName("main-view-form");
    return form;
  }

  private HorizontalLayout setupStatusesDescription() {
    VerticalLayout plannedLayout = setupStatusDescriptionLayout(
        "Stumble upon something interesting and mark it as planned to find it in your personal planned list.",
        VaadinIcon.CLIPBOARD_TEXT.create());
    VerticalLayout stoppedLayout = setupStatusDescriptionLayout(
        "Mark anime as stopped if you want to return to it after some time, so you won't forget about it later.",
        VaadinIcon.HOURGLASS.create());
    VerticalLayout watchedLayout = setupStatusDescriptionLayout(
        "Marking animes as watched helps keep track of how many titles are known to you.",
        VaadinIcon.CHECK_SQUARE_O.create());
    HorizontalLayout form = new HorizontalLayout(plannedLayout, stoppedLayout, watchedLayout);
    return form;
  }

  private VerticalLayout setupStatusDescriptionLayout(String labelText, Icon icon) {
    Label statusDescription = new Label(labelText);
    statusDescription.setClassName("main-view-text");
    icon.addClassName("icon");
    VerticalLayout layout = new VerticalLayout(icon, statusDescription);
    layout.setClassName("centered-frame");
    return layout;
  }
}
