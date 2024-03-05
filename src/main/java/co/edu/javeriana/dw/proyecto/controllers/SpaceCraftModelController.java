package co.edu.javeriana.dw.proyecto.controllers;

import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import co.edu.javeriana.dw.proyecto.service.SpacecraftModelService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/spacecraft-model")
public class SpaceCraftModelController {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpacecraftModelService spacecraftModelService;

    @GetMapping("/list")
    public String listSpaceCraftModels(Model model) {
        List<SpacecraftModel> models = spacecraftModelService.getAllSpacecraftModels();
        log.info("SpacecraftModels: " + models.toString());
        model.addAttribute("models", models);
        return "spacecraft-model-list";
    }

    @GetMapping("/view/{id}")
    public String viewSpaceCraftModel(Model m, @PathVariable Long id) {
        SpacecraftModel model = spacecraftModelService.getSpacecraftModelById(id);
        m.addAttribute("model", model);
        return "spacecraft-model-view";
    }

    @PostMapping(value = "/save")
    public String saveSpaceCraftModel(@Valid SpacecraftModel model, BindingResult result, Model m) {
        if(result.hasErrors()) {
            m.addAttribute("model", model);
            m.addAttribute("error", true); // Agregar atributo de error al modelo
            return "spacecraft-model-edit";
        }

        SpacecraftModel existingModel = spacecraftModelService.getSpacecraftModelById(model.getId());
        if (existingModel != null) {
            if (model.getStorage() < existingModel.getStorage()) {
                result.rejectValue("storage", "error.spacecraftModel", "La capacidad máxima no puede ser menor que la actual.");
                m.addAttribute("model", model);
                m.addAttribute("error", true); // Agregar atributo de error al modelo
                return "spacecraft-model-edit";
            }
        }

        spacecraftModelService.saveSpacecraftModel(model);
        return "redirect:/spacecraft-model/list";
    }



    @GetMapping("/delete/{id}")
    public String deleteSpaceCraftModel(Model m, @PathVariable Long id) {
        spacecraftModelService.deleteSpacecraftModel(id);
        return "redirect:/spacecraft-model/list";
    }

    @GetMapping("/edit/{id}")
    public String editSpaceCraftModel(Model m, @PathVariable Long id) {
        SpacecraftModel model = spacecraftModelService.getSpacecraftModelById(id);
        m.addAttribute("model", model);
        return "spacecraft-model-edit"; //TODO tener en cuenta que al cambiar la capacidad, no puede ser menor a la capacidad actual de la nave
    }

    @GetMapping("/search")
    public String searchSpaceCraftModel(@RequestParam(required = false) String searchText, Model m) {
        List<SpacecraftModel> models = spacecraftModelService.buscarPorNombre(searchText);
        if(searchText == null || searchText.isEmpty()) {
            models = spacecraftModelService.getAllSpacecraftModels();
        } else{
            models = spacecraftModelService.buscarPorNombre(searchText);
        }
        m.addAttribute("models", models);
        return "spacecraft-model-search";
    }
}
