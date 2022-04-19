package com.mygroup.watchlist.front.views;

import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.presenters.WatchlistPresenter;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import javax.annotation.security.PermitAll;

@Route("watchlist")
@PageTitle("Watchlist")
@PermitAll
public class WatchlistView extends AbstractView {

  private final WatchlistPresenter presenter;

  public WatchlistView(WatchlistPresenter presenter) {
    this.presenter = presenter;
    add(new MyHeader(), setupForm(), new MyFooter());
  }

  private VerticalLayout setupForm() {
    return null;
  }

}
