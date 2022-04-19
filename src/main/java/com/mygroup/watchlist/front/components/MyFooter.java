package com.mygroup.watchlist.front.components;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

@CssImport("./footer-styles/footer.css")
public class MyFooter extends Footer {

  public MyFooter() {
    HorizontalLayout layout = new HorizontalLayout();
    layout.add(setupAboutSiteLink());
    add(layout);
    setClassName("full-footer");
  }

  private Anchor setupAboutSiteLink() {
    Anchor anchor = new Anchor();
    anchor.setText("About site");
    anchor.getElement().addEventListener("click", e -> UI.getCurrent().navigate("")); // TODO
    return anchor;
  }
}
