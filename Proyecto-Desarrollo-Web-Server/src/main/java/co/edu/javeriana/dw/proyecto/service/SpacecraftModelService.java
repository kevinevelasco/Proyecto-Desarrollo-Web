package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import co.edu.javeriana.dw.proyecto.persistence.ISpacecraftModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpacecraftModelService {
    @Autowired
    private ISpacecraftModelRepository spacecraftModelRepository;
    public List<SpacecraftModel> getAllSpacecraftModels() {return spacecraftModelRepository.findAll();}
    public SpacecraftModel getSpacecraftModelById(Long id) {return spacecraftModelRepository.findById(id).orElse(null);}
    public SpacecraftModel saveSpacecraftModel(SpacecraftModel spacecraftModel) {return spacecraftModelRepository.save(spacecraftModel);}
    public void deleteSpacecraftModel(Long id) {spacecraftModelRepository.deleteById(id);}
    public List<SpacecraftModel> buscarPorNombre(String textoBusqueda) {
        return spacecraftModelRepository.findSpacecraftModelsByNameStartingWithCaseInsensitive(textoBusqueda);
    }

    public Page<SpacecraftModel> listarModelosNavesPaginable(Pageable pageable) {
        return spacecraftModelRepository.findAll(pageable);
    }

    public Page<SpacecraftModel> buscarSpacecraftModel(String name, Pageable pageable) {
        return spacecraftModelRepository.findAllByModelNameStartingWithIgnoreCase(name, pageable);
    }


    public int actualizarNombreModeloNave(Long id, String name) {
        return spacecraftModelRepository.updateSpacecraftModelName(id, name);
    }
}
