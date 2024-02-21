package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ISpacecraftRepository extends JpaRepository<Spacecraft, Long> {
}
