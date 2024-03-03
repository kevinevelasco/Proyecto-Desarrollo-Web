package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IStarRepository extends JpaRepository<Star, Long > {
    @Query("SELECT p FROM Star p WHERE LOWER(p.name) LIKE LOWER(concat(:text,'%'))")
    List<Star> findPersonsByLastNameStartingWithCaseInsensitive(@Param("text") String text);

}
