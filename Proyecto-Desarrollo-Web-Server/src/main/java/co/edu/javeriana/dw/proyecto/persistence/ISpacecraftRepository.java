package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISpacecraftRepository extends JpaRepository<Spacecraft, Long> {
    @Query("SELECT s FROM Spacecraft s WHERE LOWER(s.name) LIKE LOWER(concat(:text,'%'))")
    List<Spacecraft> findSpacecraftsByNameStartingWithCaseInsensitive(@Param("text") String text);
}
