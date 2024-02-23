package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Market;
import co.edu.javeriana.dw.proyecto.model.Planet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMarketRepository extends JpaRepository<Market, Long> {
}
