package co.edu.javeriana.dw.proyecto.controllers.oldcontrollers;

import co.edu.javeriana.dw.proyecto.model.*;
import co.edu.javeriana.dw.proyecto.service.*;
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

import java.util.List;

@Controller
@RequestMapping("/spacecraft")
public class SpaceCraftControllerOld {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpacecraftService spaceCraftService;

    @Autowired
    private SpacecraftModelService spacecraftModelService;

    @Autowired
    private PlanetService planetService;

    @GetMapping("/list")
    public String listSpaceCrafts(Model model) {
        List< Spacecraft> spacecrafts = spaceCraftService.getAllSpacecrafts();
        log.info("Spacecrafts: " + spacecrafts.toString());
        model.addAttribute("spacecrafts", spacecrafts);
        return "spacecraft-list";
    }

    @GetMapping("/view/{id}")
    public String viewSpaceCraft(Model model, @PathVariable Long id) {
        Spacecraft spacecraft = spaceCraftService.getSpacecraftById(id);
        model.addAttribute("spacecraft", spacecraft);
        return "spacecraft-view";
    }

    @PostMapping(value = "/save")
    public String saveSpaceCraft(@Valid Spacecraft spacecraft, BindingResult result, Model model) {
        if(result.hasErrors()) {
            List<SpacecraftModel> spacecraftModels = spacecraftModelService.getAllSpacecraftModels();
            List<Planet> planets = planetService.getAllPlanets();

            model.addAttribute("spacecraft", spacecraft);
            model.addAttribute("models", spacecraftModels); //TODO el modelo que cambie debe satisfacer las restricciones de capacidad de la nave, es decir la del inventario
            model.addAttribute("planets", planets);
            return "spacecraft-edit";
        }
        spaceCraftService.saveSpacecraft(spacecraft);
        return "redirect:/spacecraft/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteSpaceCraft(Model model, @PathVariable Long id) {
        spaceCraftService.deleteSpacecraft(id);
        return "redirect:/spacecraft/list";
    }

    @GetMapping("/edit/{id}")
    public String editSpaceCraft(Model model, @PathVariable Long id) {
        Spacecraft spacecraft = spaceCraftService.getSpacecraftById(id);
        List<SpacecraftModel> spacecraftModels = spacecraftModelService.getAllSpacecraftModels();
        List<Planet> planets = planetService.getAllPlanets();
        model.addAttribute("spacecraft", spacecraft);
        model.addAttribute("models", spacecraftModels); //TODO el modelo que cambie debe satisfacer las restricciones de capacidad de la nave, es decir la del inventario
        model.addAttribute("planets", planets);
        return "spacecraft-edit";
    }

    @GetMapping("/search")
    public String listSpaceCrafts(@RequestParam(required = false) String searchText, Model model) {
        List<Spacecraft> spacecrafts;
        if(searchText == null || searchText.isEmpty()) {
            spacecrafts = spaceCraftService.getAllSpacecrafts();
        } else {
            spacecrafts = spaceCraftService.buscarPorNombre(searchText);
        }
        model.addAttribute("spacecrafts", spacecrafts);
        return "spacecraft-search";
    }

    @GetMapping("/create")
    public String createSpaceCraft(Model model) {
        List<SpacecraftModel> spacecraftModels = spacecraftModelService.getAllSpacecraftModels();
        List<Planet> planets = planetService.getAllPlanets();
        model.addAttribute("models", spacecraftModels);
        model.addAttribute("planets", planets);
        model.addAttribute("spacecraft", new Spacecraft());
        return "spacecraft-create";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "* ERROR: No se puede eliminar la nave porque tiene jugadores e inventarios asociados");
        return "redirect:/spacecraft/list";
    }

}
