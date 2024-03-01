package co.edu.javeriana.dw.proyecto.controllers;

import co.edu.javeriana.dw.proyecto.model.Star;
import co.edu.javeriana.dw.proyecto.service.StarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Controller
@RequestMapping("/star")
public class StarController {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private StarService starService;

    @GetMapping("/list")
    public String listStars(Model model) {
        List<Star> stars = starService.getAllStars();
        log.info("Estrellas: " + stars.size());
        model.addAttribute("stars", stars);
        return "star-list";
    }

    @GetMapping("/view/{id}")
    public String viewStar(@PathVariable("id") Long id, Model model) {
        Star star = starService.getStarById(id);
        model.addAttribute("star", star);
        return "star-view";
    }

    @GetMapping("/edit/{id}")
    public String editStarForm(@PathVariable Long id, Model model) {
        Star star = starService.getStarById(id);
        model.addAttribute("star", star);
        return "star-edit";
    }

    @PostMapping(value = "/save")
    public String saveStar(@Valid Star star, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("star", star);
            return "star-edit";
        }
        starService.saveStar(star);
        return "redirect:/star/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteStar(@PathVariable Long id) {
        if (starService.deleteStar(id)) {
            log.info("Star with id {} was deleted successfully", id);
        } else {
            log.info("Star with id {} not found or could not be deleted", id);
        }
        return "redirect:/star/list";
    }
}
