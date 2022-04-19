package com.mygroup.watchlist.front.views;

import com.mygroup.watchlist.back.entities.User;
import com.mygroup.watchlist.front.components.BasicUpload;
import com.mygroup.watchlist.front.components.MyFooter;
import com.mygroup.watchlist.front.components.MyHeader;
import com.mygroup.watchlist.front.presenters.ProfilePresenter;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.button.Button;
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
import javax.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

@Route("profile")
@PageTitle("Profile")
@PermitAll
public class ProfileView extends AbstractView {

  private final ProfilePresenter presenter;
  private User user;

  @Autowired
  public ProfileView(ProfilePresenter presenter) {
    this.presenter = presenter;
  }

  @Override
  protected void onAttach(AttachEvent event) {
    user = presenter.getCurrentUser();
    add(new MyHeader(), setupForm(), new MyFooter());
  }

  private HorizontalLayout setupForm() {
    HorizontalLayout textAndProfilePicture =
        new HorizontalLayout(setupProfilePictureLayout(), setupUserDataLayout());
    return textAndProfilePicture;
  }

  private VerticalLayout setupProfilePictureLayout() {
    Image profilePicture =
        new Image(presenter.getProfilePictureStreamResource(user), "profile picture");

    BasicUpload profilePictureUpload = setupProfilePictureUpload(profilePicture);

    VerticalLayout profilePictureLayout = new VerticalLayout(profilePicture, profilePictureUpload);
    return profilePictureLayout;
  }

  private VerticalLayout setupUserDataLayout() {
    H2 username = new H2(user.getUsername());
    H3 userEmail = new H3(user.getEmail());
    VerticalLayout dataLayout = new VerticalLayout(username, userEmail);
    return dataLayout;
  }

  private BasicUpload setupProfilePictureUpload(Image profilePicture) {
    BasicUpload profilePictureUpload = new BasicUpload("Change profile picture");
    profilePictureUpload.setDropLabelIcon(new Label());
    profilePictureUpload.setUploadButton(new Button(new Icon(VaadinIcon.UPLOAD_ALT)));
    profilePictureUpload.addFinishedListener(event -> {
      user = presenter.updateProfilePicture(user, profilePictureUpload.getValue());
      profilePicture.setSrc(presenter.getProfilePictureStreamResource(user));
      profilePictureUpload.getElement().executeJs("this.files=[]");
    });
    return profilePictureUpload;
  }
}
