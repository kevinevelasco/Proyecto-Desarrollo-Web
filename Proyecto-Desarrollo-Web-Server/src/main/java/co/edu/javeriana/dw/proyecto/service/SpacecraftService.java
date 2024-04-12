package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.persistence.ISpacecraftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpacecraftService {
    @Autowired
    private ISpacecraftRepository spacecraftRepository;
    public List<Spacecraft> getAllSpacecrafts() {return spacecraftRepository.findAll();}
    public Spacecraft getSpacecraftById(Long id) {return spacecraftRepository.findById(id).orElse(null);} //TODO que en orElse retorne de una vez el código del error
    public Spacecraft saveSpacecraft(Spacecraft spacecraft) {return spacecraftRepository.save(spacecraft);}
    public void deleteSpacecraft(Long id) {spacecraftRepository.deleteById(id);}
    public List<Spacecraft> buscarPorNombre(String textoBusqueda) {
        return spacecraftRepository.findSpacecraftsByNameStartingWithCaseInsensitive(textoBusqueda);
    }

    public Page<Spacecraft> listarNavesPaginable(Pageable pageable) {
        return spacecraftRepository.findAll(pageable);
    }

    public Page<Spacecraft> buscarSpacecraft(String name, Pageable pageable) {
        return spacecraftRepository.findAllByNameStartingWithIgnoreCase(name, pageable);
    }

    public int actualizarNombreNave(Long id, String name) {
        return spacecraftRepository.updateSpacecraftName(id, name);
    }

    public List<Spacecraft> getSpacecraftsByPlanetId(Long planetId) {
        return spacecraftRepository.findSpacecraftsByPlanetId(planetId);
    }
}
