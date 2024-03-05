package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.persistence.ISpacecraftRepository;
import co.edu.javeriana.dw.proyecto.persistence.IStarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpacecraftService {
    @Autowired
    private ISpacecraftRepository spacecraftRepository;
    public List<Spacecraft> getAllSpacecrafts() {return spacecraftRepository.findAll();}
    public Spacecraft getSpacecraftById(Long id) {return spacecraftRepository.findById(id).orElse(null);}
    public Spacecraft saveSpacecraft(Spacecraft spacecraft) {return spacecraftRepository.save(spacecraft);}
    public void deleteSpacecraft(Long id) {spacecraftRepository.deleteById(id);}
    public List<Spacecraft> buscarPorNombre(String textoBusqueda) {
        return spacecraftRepository.findSpacecraftsByNameStartingWithCaseInsensitive(textoBusqueda);
    }

}
