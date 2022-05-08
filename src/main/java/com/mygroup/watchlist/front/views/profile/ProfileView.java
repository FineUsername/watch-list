package com.mygroup.watchlist.front.views.profile;

import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.front.components.BasicUpload;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.views.AbstractView;
import com.mygroup.watchlist.front.views.profile.change.ChangeDataView;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinServletRequest;
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("profile")
@PageTitle("Profile")
@PermitAll
@StyleSheet("context://frontend/profile-styles/profile.css")
public class ProfileView extends AbstractView {

  private final ProfilePresenter presenter;
  private User user;

  @Autowired
  public ProfileView(ProfilePresenter presenter) {
    this.presenter = presenter;
    addClassName("centered-frame");
  }

  @Override
  protected void onAttach(AttachEvent event) {
    user = presenter.getCurrentUser();
    add(new MyHeader(), setupForm(), new MyFooter());
  }

  private VerticalLayout setupForm() {
    HorizontalLayout textAndPicture =
        new HorizontalLayout(setupPictureLayout(), setupUserDataLayout());
    textAndPicture.setClassName("text-and-picture-layout");
    VerticalLayout form = new VerticalLayout(textAndPicture, setupButtonsLayout());
    form.getStyle().clear();
    form.setClassName("profile-form");
    return form;
  }

  private VerticalLayout setupPictureLayout() {
    Image picture = new Image(presenter.getProfilePictureStreamResource(user), "profile picture");
    picture.setClassName("picture");

    BasicUpload pictureUpload = setupProfilePictureUpload(picture);
    pictureUpload.setClassName("picture-upload");

    VerticalLayout pictureLayout = new VerticalLayout(picture, pictureUpload);
    pictureLayout.getStyle().clear();
    pictureLayout.setClassName("picture-layout");
    return pictureLayout;
  }

  private VerticalLayout setupUserDataLayout() {
    H2 username = new H2(user.getUsername());
    username.setClassName("text-data");
    H3 userEmail = new H3(user.getEmail());
    userEmail.setClassName("text-data");
    VerticalLayout dataLayout = new VerticalLayout(username, userEmail);
    dataLayout.getStyle().clear();
    dataLayout.setClassName("data-layout");
    return dataLayout;
  }

  private BasicUpload setupProfilePictureUpload(Image profilePicture) {
    BasicUpload profilePictureUpload = new BasicUpload("Change picture");
    profilePictureUpload.setDropLabelIcon(new Label());
    profilePictureUpload.setUploadButton(new Button(new Icon(VaadinIcon.UPLOAD_ALT)));
    profilePictureUpload.addFinishedListener(event -> {
      user = presenter.updateProfilePicture(user, profilePictureUpload.getValue());
      profilePicture.setSrc(presenter.getProfilePictureStreamResource(user));
      profilePictureUpload.getElement().executeJs("this.files=[]");
    });
    return profilePictureUpload;
  }

  private HorizontalLayout setupButtonsLayout() {
    Button changePersonalData =
        new Button("Change personal data", click -> UI.getCurrent().navigate(ChangeDataView.class));
    changePersonalData.setClassName("blue-button");
    changePersonalData.addClassName("button-profile");
    Button logout = new Button("Logout", click -> {
      UI.getCurrent().getPage().setLocation("/");
      presenter.logout(VaadinServletRequest.getCurrent().getHttpServletRequest());
    });
    logout.setClassName("blue-button");
    logout.addClassName("button-profile");
    HorizontalLayout buttonsLayout = new HorizontalLayout(changePersonalData, logout);
    buttonsLayout.getStyle().clear();
    return buttonsLayout;
  }
}
