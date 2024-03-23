package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISpacecraftModelRepository extends JpaRepository<SpacecraftModel, Long> {
    @Query("SELECT s FROM SpacecraftModel s WHERE LOWER(s.modelName) LIKE LOWER(concat(:text,'%'))")
    List<SpacecraftModel> findSpacecraftModelsByNameStartingWithCaseInsensitive(@Param("text") String text);

    Page<SpacecraftModel> findAllByModelNameStartingWithIgnoreCase(String modelName, Pageable pageable);
    @Transactional
    @Modifying
    @Query("UPDATE SpacecraftModel s SET s.modelName = :name WHERE s.id = :id")
    int updateSpacecraftModelName(Long id, String name);
}
