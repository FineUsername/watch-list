package com.mygroup.watchlist.front.components;

import com.mygroup.watchlist.dto.AnimePreviewDto;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class AnimePreviewLayout extends HorizontalLayout {

  private AnimePreviewDto dto;
  private Image previewPicture = new Image();
  private H2 title = new H2();
  private H3 description = new H3();
  private H2 status = new H2();
  private boolean statusEnabled = false;

  public AnimePreviewLayout(AnimePreviewDto dto) {
    this.dto = dto;
    previewPicture.setAlt("preview picture");
    fillLayout();
    add(previewPicture, new VerticalLayout(title, description));
  }

  public void setStatusEnabled(boolean statusEnabled) {
    this.statusEnabled = statusEnabled;
  }

  public void changeAnime(AnimePreviewDto dto) {
    this.dto = dto;
    fillLayout();
  }

  private void fillLayout() {
    previewPicture.setSrc(dto.getPreviewPicture());
    title.setText(dto.getTitle());
    description.setText(dto.getDescription());
    // TODO show status
  }

}
