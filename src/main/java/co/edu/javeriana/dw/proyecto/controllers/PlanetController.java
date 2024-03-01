package co.edu.javeriana.dw.proyecto.controllers;

import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.service.PlanetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.validation.Valid;


import java.util.List;

@Controller
@RequestMapping("/planet")
public class PlanetController {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PlanetService planetService;

    @GetMapping("/list")
    public String listPlanets(Model model) {
        List<Planet> planets = planetService.getAllPlanets();
        log.info("Planets: " + planets.size());
        model.addAttribute("planets", planets);
        return "planet-list";
    }
    @GetMapping("/view/{id}")
    public String viewPlanet(Model model, Long  id) {
        Planet planet = planetService.getPlanetById(id);
        model.addAttribute("planet", planet);
        return "planet-view";
    }
    @GetMapping("/delete/{id}")
    public String deletePlanet(Model model, Long  id) {
        planetService.deletePlanet(id);
        return "redirect:/planet/list";
    }
    @GetMapping("/edit/{id}")
    public String editPlanet(Model model, Long  id) {
        Planet planet = planetService.getPlanetById(id);
        model.addAttribute("planet", planet);
        return "planet-edit";
    }

    @PostMapping(value = "/save")
    public String savePlanet(@Valid Planet planet, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "planet-edit";
        }
        planetService.savePlanet(planet);
        return "redirect:/planet/list";
    }
}
