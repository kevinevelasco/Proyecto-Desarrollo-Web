package co.edu.javeriana.dw.proyecto.controllers.oldcontrollers;

import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.model.Star;
import co.edu.javeriana.dw.proyecto.service.PlanetService;
import co.edu.javeriana.dw.proyecto.service.StarService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLDataException;
import java.util.List;

@Controller
@RequestMapping("/planet")
public class PlanetControllerOld {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PlanetService planetService;

    @Autowired
    private StarService starService;

    @GetMapping("/list")
    public String listPlanets(Model model) {
        List<Planet> planets = planetService.getAllPlanets();
        log.info("Planets: " + planets.size());
        model.addAttribute("planets", planets);
        return "planet-list";
    }

    @GetMapping("/view/{id}")
    public String viewPlanet(Model model, @PathVariable Long id) {
        Planet planet = planetService.getPlanetById(id);
        model.addAttribute("planet", planet);
        return "planet-view";
    }

    @PostMapping(value = "/save")
    public String savePlanet(@Valid Planet planet, BindingResult result, Model model) {
        log.info("Saving planet with ID: {}", planet.getId()); // Agregar esta línea para depuración
        result.getAllErrors().forEach(e -> log.info("Error: {}", e.getDefaultMessage())); // Agregar esta línea para depuración
        if (result.hasErrors()) {
            List<Star> stars = starService.getAllStars();
            model.addAttribute("stars", stars);
            model.addAttribute("planet", planet);
            return "planet-edit";
        }
        planetService.savePlanet(planet);
        return "redirect:/planet/list";
    }


    @GetMapping("/delete/{id}")
    public String deletePlanet(@PathVariable  Long id) {
        planetService.deletePlanet(id);
        return "redirect:/planet/list";
    }

    @GetMapping("/edit/{id}")
    public String editPlanet(Model model, @PathVariable Long id) {
        Planet planet = planetService.getPlanetById(id);
        model.addAttribute("planet", planet);
        return "planet-edit";
    }

    @GetMapping("/search")
    public String listPlanets(@RequestParam(required = false) String searchText, Model model) {
        List<Planet> planets;
        if (searchText == null || searchText.trim().equals("")) {
            log.info("No hay texto de búsqueda. Retornando todo");
            planets = planetService.getAllPlanets();
        } else {
            log.info("Buscando estrellas cuyo nombre comienza con {}", searchText);
            planets = planetService.buscarPorNombre(searchText);
        }
        model.addAttribute("planets", planets);
        return "planet-search";
    }

    @GetMapping("/create")
    public String createPlanet(Model model) {
        List<Star> stars = starService.getAllStars();
        model.addAttribute("stars", stars);
        model.addAttribute("planet", new Planet());
        return "planet-create";
    }

    @ExceptionHandler({DataIntegrityViolationException.class, SQLDataException.class})
    public String handleDataIntegrityViolationException(DataIntegrityViolationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "* ERROR: No se puede eliminar el planeta porque tiene otras entidades asociadas");
        return "redirect:/planet/list";
    }

}
