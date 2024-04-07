package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.model.Product;
import co.edu.javeriana.dw.proyecto.service.PlanetService;

@RestController
@RequestMapping("/api/planet")
public class PlanetController {

    @Autowired
    private PlanetService planetService;

    // http://localhost:8080/api/planet/{id}
    @GetMapping("/{id}")
    public Planet getPlanet(@PathVariable Long id) {
        return planetService.getPlanetById(id);
    }

    // http://localhost:8080/api/planet/{id}/products
    @GetMapping("/{id}/products")
    public List<Product> getPlanetProducts(@PathVariable Long id) {
        return planetService.getProductsByPlanet(id);
    }

    
}
