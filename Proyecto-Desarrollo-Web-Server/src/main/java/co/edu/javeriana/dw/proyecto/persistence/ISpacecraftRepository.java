package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ISpacecraftRepository extends JpaRepository<Spacecraft, Long> {
    @Query("SELECT s FROM Spacecraft s WHERE LOWER(s.name) LIKE LOWER(concat(:text,'%'))")
    List<Spacecraft> findSpacecraftsByNameStartingWithCaseInsensitive(@Param("text") String text);

    Page<Spacecraft> findAllByNameStartingWithIgnoreCase(String name, Pageable pageable);

    @Transactional
    @Modifying
    @Query("UPDATE Spacecraft s SET s.name = :name WHERE s.id = :id")
    int updateSpacecraftName(@Param("id") Long id, @Param("name") String name);
}
