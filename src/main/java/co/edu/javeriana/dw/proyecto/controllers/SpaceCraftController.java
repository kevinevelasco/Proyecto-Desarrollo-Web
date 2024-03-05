package co.edu.javeriana.dw.proyecto.controllers;

import co.edu.javeriana.dw.proyecto.model.*;
import co.edu.javeriana.dw.proyecto.service.*;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/spacecraft")
public class SpaceCraftController {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpacecraftService spaceCraftService;

    @Autowired
    private SpacecraftModelService spacecraftModelService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private PlanetService planetService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

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
            model.addAttribute("spacecraft", spacecraft);
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
        List<Player> players = playerService.getAllPlayers();
        List<Planet> planets = planetService.getAllPlanets();
        List<Inventory> inventories = inventoryService.getAllInventories();
        List<Product> products = productService.getAllProduct();
        model.addAttribute("spacecraft", spacecraft);
        model.addAttribute("models", spacecraftModels); //TODO el modelo que cambie debe satisfacer las restricciones de capacidad de la nave, es decir la del inventario
        model.addAttribute("players", players);
        model.addAttribute("planets", planets);
        model.addAttribute("products", products);
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

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {
        dataBinder.setAutoGrowCollectionLimit(600);
    }

}
