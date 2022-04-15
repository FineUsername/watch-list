package com.mygroup.watchlist.controllers;

import com.mygroup.watchlist.entities.Anime;
import com.mygroup.watchlist.forms.AnimeForm;
import com.mygroup.watchlist.services.AnimeService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("anime")
public class AnimeController {

  private final AnimeService animeService;

  @Autowired
  public AnimeController(AnimeService animeService) {
    this.animeService = animeService;
  }

  @GetMapping("{id}")
  public String getAnime(@PathVariable long id, Model model) {
    model.addAttribute("anime", animeService.findById(id));
    System.out.println("good");
    return "anime";
  }

  @GetMapping("add")
  public String addAnime(Model model) {
    model.addAttribute("animeForm", new AnimeForm());
    return "add-anime";
  }

  @PostMapping("add")
  public String addAnime(@Valid @ModelAttribute("animeForm") AnimeForm animeForm,
      BindingResult bindingResult, Model model) {
    if (bindingResult.hasErrors()) {
      model.addAttribute("animeForm", animeForm);
      return "add-anime";
    }
    try {
      Anime anime = animeService.add(animeForm);
      return "redirect:/anime/" + anime.getId();
    } catch (Exception e) {
      bindingResult.rejectValue("title", "title", "Page for this title already exists");
      model.addAttribute("animeForm", animeForm);
      return "add-anime";
    }
  }

  @GetMapping("all")
  public String getAllAnimes(Model model) {
    model.addAttribute("animes", animeService.findAll());
    return "anime-list";
  }

  @DeleteMapping("{id}")
  public String deleteAnime(@PathVariable long id) {
    animeService.deleteById(id);
    System.out.println("bad");
    // TODO redirect does not add attribute to model
    return "redirect:/anime-list";
  }

  @GetMapping("search")
  public String searchTitle(@RequestParam("string") String string, Model model) {
    model.addAttribute("animes", animeService.searchTitle(string));
    return "anime-list";
  }
}
