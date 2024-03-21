package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Star;
import co.edu.javeriana.dw.proyecto.persistence.IStarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StarService {

    @Autowired
    private IStarRepository starRepository;

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

}
