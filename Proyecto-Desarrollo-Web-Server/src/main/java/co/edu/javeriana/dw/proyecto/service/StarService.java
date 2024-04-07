package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Star;
import co.edu.javeriana.dw.proyecto.persistence.IPlanetRepository;
import co.edu.javeriana.dw.proyecto.persistence.IStarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Comparator;
import java.util.stream.Collectors;
import co.edu.javeriana.dw.proyecto.model.Planet;



import java.util.List;

@Service
public class StarService {

    @Autowired
    private IStarRepository starRepository;

    @Autowired
    private IPlanetRepository planetRepository;

    public List<Star> getAllStars() {
        return starRepository.findAll();
    }

    public Star getStarById(Long id) {
        return starRepository.findById(id).orElse(null);
    }

    public Star saveStar(Star star) {
        return starRepository.save(star);
    }

    public void deleteStar(Long id) {
        starRepository.deleteById(id);
    }

    public int actualizarNombreEstrella(Long id, String name) {
        return starRepository.updateStarName(id, name);
    }
    public Page<Star> listarEstrellasPaginable(Pageable pageable) {
        return starRepository.findAll(pageable);
    }

    public Page<Star> buscarEstrella(String name, Pageable pageable) {
        return starRepository.findAllByNameStartingWithIgnoreCase(name, pageable);
    }

    public List<Star> findNearestStars(Long currentStarId, int limit) {
        Star currentStar = starRepository.findById(currentStarId).orElseThrow(() -> new RuntimeException("Estrella no encontrada"));
        List<Star> allStars = starRepository.findAll();
        return allStars.stream()
                .filter(star -> !star.getId().equals(currentStarId)) // esto quita para q no se muestre la estrella donde esta
                .sorted(Comparator.comparingDouble(star -> calculateDistance(currentStar, star)))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private double calculateDistance(Star fromStar, Star toStar) {
        return Math.sqrt(Math.pow(toStar.getX() - fromStar.getX(), 2) +
                Math.pow(toStar.getY() - fromStar.getY(), 2) +
                Math.pow(toStar.getZ() - fromStar.getZ(), 2));
    }

    public List<Planet> findPlanetsByStarId(Long starId) {
        Star star = starRepository.findById(starId).orElseThrow(() -> new RuntimeException("Estrella no encontrada"));
        return star.getPlanets();
    }

    public Star getStarByPlayer(Long id) {
        Planet currentPlanet = planetRepository.findById(id).orElseThrow(() -> new RuntimeException("Planeta no encontrado"));
        return currentPlanet.getStar();
    }
}
