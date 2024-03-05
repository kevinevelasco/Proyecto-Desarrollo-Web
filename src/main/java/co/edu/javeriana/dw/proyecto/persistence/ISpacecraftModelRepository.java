package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISpacecraftModelRepository extends JpaRepository<SpacecraftModel, Long> {
    @Query("SELECT s FROM SpacecraftModel s WHERE LOWER(s.modelName) LIKE LOWER(concat(:text,'%'))")
    List<SpacecraftModel> findSpacecraftModelsByNameStartingWithCaseInsensitive(@Param("text") String text);
}
