package com.mygroup.watchlist.front.views;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

@CssImport("./abstract-styles/abstract.css")
@CssImport(value = "./vtextfield.css", themeFor = "vaadin-text-field")
@CssImport(value = "./vtextfield.css", themeFor = "vaadin-email-field")
@CssImport(value = "./vtextfield.css", themeFor = "vaadin-password-field")
@CssImport(value = "./vtextfield.css", themeFor = "vaadin-text-area")
@CssImport(value = "./vtextfield.css", themeFor = "vaadin-combo-box")
@CssImport(value = "./vtextfield.css", themeFor = "vaadin-checkbox")
@CssImport(value = "./vupload.css", themeFor = "vaadin-upload")
@CssImport(value = "./vupload.css", themeFor = "vaadin-upload-file")
@CssImport(value = "./vbutton.css", themeFor = "vaadin-button")
public abstract class AbstractView extends Div {

  public void showNotification(String message, int durationMillis, Position position,
      NotificationVariant theme) {
    Notification notification = Notification.show(message, durationMillis, position);
    notification.addThemeVariants(theme);
  }

  public AbstractView() {
    setClassName("background");
  }
}
