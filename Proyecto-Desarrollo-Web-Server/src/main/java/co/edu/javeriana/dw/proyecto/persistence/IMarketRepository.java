package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Market;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IMarketRepository extends JpaRepository<Market, Long> {
    @Query("SELECT m FROM Market m WHERE LOWER(m.product.name) LIKE LOWER(concat(:text, '%'))")
    List<Market> findPlanetsByNameStartingWithCaseInsensitive(@Param("text") String text);

    Page<Market> findAllByPlanetId(Long planetId, Pageable pageable);

    @Query("SELECT m FROM Market m WHERE m.planet.id = :planetId")
    List<Market> findAllByPlanetId(Long planetId);
}
