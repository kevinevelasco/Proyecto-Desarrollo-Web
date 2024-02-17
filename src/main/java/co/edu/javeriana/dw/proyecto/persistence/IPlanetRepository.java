package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlanetRepository extends JpaRepository<Planet, Long> {
}
