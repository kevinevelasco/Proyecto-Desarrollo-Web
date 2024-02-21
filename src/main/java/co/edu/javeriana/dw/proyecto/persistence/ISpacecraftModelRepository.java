package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISpacecraftModelRepository extends JpaRepository<SpacecraftModel, Long> {
}
