package com.mygroup.watchlist.front.components;

import com.mygroup.watchlist.front.views.AbstractView;
import com.mygroup.watchlist.front.views.AllAnimeView;
import com.mygroup.watchlist.front.views.AddAnimeView;
import com.mygroup.watchlist.front.views.ProfileView;
import com.mygroup.watchlist.front.views.WatchlistView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@CssImport("./header-styles/header.css")
public class MyHeader extends Header {

  public MyHeader() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.add(setupLinkButton("Main page", null)); // TODO
    layout.add(setupLinkButton("Browse anime", AllAnimeView.class));
    layout.add(setupLinkButton("Watchlist", WatchlistView.class));
    layout.add(setupLinkButton("Add anime", AddAnimeView.class));
    layout.add(setupLinkButton("Profile", ProfileView.class));
    add(layout);
    setWidthFull();
    layout.setClassName("full-header");
  }

  private Button setupLinkButton(String title, Class<? extends AbstractView> view) {
    Button button = new Button(title);
    button.addClickListener(event -> UI.getCurrent().navigate(view));
    return button;
  }

}
