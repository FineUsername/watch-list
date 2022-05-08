package com.mygroup.watchlist.front.components;

import com.mygroup.watchlist.front.views.about.AboutSiteView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
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

  private Button setupAboutSiteLink() {
    Button button = new Button("About site");
    button.setClassName("button");
    button.addClickListener(click -> UI.getCurrent().navigate(AboutSiteView.class));
    return button;
  }
}
