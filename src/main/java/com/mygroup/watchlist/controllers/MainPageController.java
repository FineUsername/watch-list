package com.mygroup.watchlist.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("mainpage")
public class MainPageController {

  @GetMapping
  public String getMainPage() {
    return "mainpage";
  }

}
