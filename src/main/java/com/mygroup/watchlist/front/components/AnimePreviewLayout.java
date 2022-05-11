package com.mygroup.watchlist.front.components;

import com.mygroup.watchlist.dto.AnimePreviewDto;
import com.mygroup.watchlist.front.views.anime.AnimeView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;

public class AnimePreviewLayout extends HorizontalLayout {

  private AnimePreviewDto dto;
  private Image previewPicture = new Image();
  private H2 title = new H2();
  private H3 description = new H3();
  private H2 status = new H2();

  public AnimePreviewLayout(AnimePreviewDto dto) {
    getStyle().clear();
    setClassName("preview-form");
    this.dto = dto;
    previewPicture.setAlt("preview picture");
    previewPicture.setClassName("preview-picture");
    fillLayout();
    add(previewPicture, new VerticalLayout(title, status, description));
  }

  public void changeAnime(AnimePreviewDto dto) {
    this.dto = dto;
    fillLayout();
  }

  private void fillLayout() {
    addClickListener(
        click -> UI.getCurrent().navigate(AnimeView.class, String.valueOf(dto.getId())));
    previewPicture.setSrc(new StreamResource("", () -> dto.getPicture()));
    title.setText(dto.getTitle());
    title.setClassName("text");
    description.setText(dto.getDescription());
    description.setClassName("text");
    status.setText(dto.getStatusRepresentation());
    status.setClassName("text");
  }

}
