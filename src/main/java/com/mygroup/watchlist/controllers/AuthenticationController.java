package com.mygroup.watchlist.controllers;

import com.mygroup.watchlist.entities.User;
import com.mygroup.watchlist.exceptions.UsernameAlreadyTakenException;
import com.mygroup.watchlist.forms.RegistrationForm;
import com.mygroup.watchlist.services.UserService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("auth")
public class AuthenticationController {

  private final UserService userService;

  @Autowired
  public AuthenticationController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping("login")
  public String login() {
    return "login";
  }

  @GetMapping("register")
  public String register(Model model) {
    model.addAttribute("registrationForm", new RegistrationForm());
    return "register";
  }

  @PostMapping("register")
  public String register(
      @Valid @ModelAttribute("registrationForm") RegistrationForm registrationForm,
      BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("registrationForm", registrationForm);
      return "register";
    }
    try {
      User user = userService.register(registrationForm);
      return "redirect:/mainpage";
    } catch (UsernameAlreadyTakenException e) {
      bindingResult.rejectValue("username", "username",
          "An account already exists for this username");
      model.addAttribute("registrationForm", registrationForm);
      return "register";
    }
  }

}
