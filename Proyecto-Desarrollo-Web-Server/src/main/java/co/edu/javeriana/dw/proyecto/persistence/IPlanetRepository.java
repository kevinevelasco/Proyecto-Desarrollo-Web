package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPlanetRepository extends JpaRepository<Planet, Long> {
    @Query("SELECT p FROM Planet p WHERE LOWER(p.name) LIKE LOWER(concat(:text, '%'))")
    List<Planet> findPlanetsByNameStartingWithCaseInsensitive(@Param("text") String text);
}
