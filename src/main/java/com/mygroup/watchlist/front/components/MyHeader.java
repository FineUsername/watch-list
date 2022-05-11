package com.mygroup.watchlist.front.components;

import com.mygroup.watchlist.back.security.SecurityUtils;
import com.mygroup.watchlist.front.views.AbstractView;
import com.mygroup.watchlist.front.views.anime.add.AddAnimeView;
import com.mygroup.watchlist.front.views.anime.browse.BrowseAnimeView;
import com.mygroup.watchlist.front.views.main.MainView;
import com.mygroup.watchlist.front.views.profile.ProfileView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@CssImport("./header-styles/header.css")
public class MyHeader extends Header {

  public MyHeader() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.add(setupLinkButton("Main page", MainView.class));
    layout.add(setupLinkButton("Browse anime", BrowseAnimeView.class));
    if (SecurityUtils.isAdminAuthenticated()) {
      layout.add(setupLinkButton("Add anime", AddAnimeView.class));
    }
    layout.add(setupLinkButton("Profile", ProfileView.class));
    layout.setClassName("full-header");
    add(layout);
    setWidthFull();
  }

  private Button setupLinkButton(String title, Class<? extends AbstractView> view) {
    Button button = new Button(title);
    button.setClassName("button");
    button.addClickListener(event -> UI.getCurrent().navigate(view));
    return button;
  }

}
