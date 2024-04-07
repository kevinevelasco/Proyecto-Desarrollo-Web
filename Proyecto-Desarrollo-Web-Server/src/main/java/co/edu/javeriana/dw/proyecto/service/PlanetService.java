package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.model.Product;
import co.edu.javeriana.dw.proyecto.model.Star;
import co.edu.javeriana.dw.proyecto.persistence.IPlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlanetService {

    @Autowired
    private IPlanetRepository planetRepository;

    public List<Planet> getAllPlanets() {
        return planetRepository.findAll();
    }

    public Planet getPlanetById(Long id) {
        return planetRepository.findById(id).orElse(null);
    }

    public Planet savePlanet(Planet planet) {
        return planetRepository.save(planet);
    }


    public void deletePlanet(Long id) {
        planetRepository.deleteById(id);
    }

    public List<Planet> buscarPorNombre(String textoBusqueda) {
        return planetRepository.findPlanetsByNameStartingWithCaseInsensitive(textoBusqueda);
    }

    public List<Product> getProductsByPlanet(Long id) {
        return planetRepository.findById(id).orElse(null).getProducts();
    }

}
