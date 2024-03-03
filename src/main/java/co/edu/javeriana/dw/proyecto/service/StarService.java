package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Star;
import co.edu.javeriana.dw.proyecto.persistence.IStarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StarService {

    @Autowired
    private IStarRepository starRepository;

    public List<Star> getAllStars() {return starRepository.findAll();}

    public Star getStarById(Long id) {return starRepository.findById(id).orElse(null);}

    public Star saveStar(Star star) {return starRepository.save(star);}

    public boolean deleteStar(Long id) {starRepository.deleteById(id);
        return false;
    }

    public List<Star> buscarPorNombre(String textoBusqueda) {
        return starRepository.findPersonsByLastNameStartingWithCaseInsensitive(textoBusqueda);
    }

}
