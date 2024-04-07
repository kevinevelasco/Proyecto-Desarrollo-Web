package co.edu.javeriana.dw.proyecto.controllers.oldcontrollers;

import co.edu.javeriana.dw.proyecto.model.Star;
import co.edu.javeriana.dw.proyecto.service.StarService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/star")
public class StarControllerOld {

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
    public String editStarForm(Model model, @PathVariable Long id) {
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
        starService.deleteStar(id);
        return "redirect:/star/list";
    }

    /*@GetMapping("/search")
    public String listStars(@RequestParam(required = false) String searchText, Model model) {
        List<Star> stars;
        if (searchText == null || searchText.trim().equals("")) {
            log.info("No hay texto de búsqueda. Retornando todo");
            stars = starService.getAllStars();
        } else {
            log.info("Buscando estrellas cuyo nombre comienza con {}", searchText);
            //stars = starService.buscarPorNombre(searchText);
        }
        //model.addAttribute("stars", stars);
        return "star-search";
    }*/
    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "* ERROR: No se puede eliminar la estrella porque tiene planetas asociados");
        return "redirect:/star/list";
    }

    @GetMapping("/create")
    public String createStar(Model model) {
        model.addAttribute("star", new Star());
        return "star-create";
    }

}
