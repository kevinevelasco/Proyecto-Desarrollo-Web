package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Star;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IStarRepository extends JpaRepository<Star, Long > {
    @Transactional
    @Modifying
    @Query("UPDATE Star s SET s.name = :name WHERE s.id = :id")
    int updateStarName(@Param("id") Long id, @Param("name") String name);

    Page<Star> findAllByNameStartingWithIgnoreCase(String name, Pageable pageable);
}
