package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.model.Star;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IStarRepository extends JpaRepository<Star, Long > {
}
