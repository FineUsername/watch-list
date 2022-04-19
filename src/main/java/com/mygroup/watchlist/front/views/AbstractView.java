package com.mygroup.watchlist.front.views;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;

public abstract class AbstractView extends Div {

  public void showNotification(String message, int durationMillis, Position position,
      NotificationVariant theme) {
    Notification notification = Notification.show(message, durationMillis, position);
    notification.addThemeVariants(theme);
  }
}
