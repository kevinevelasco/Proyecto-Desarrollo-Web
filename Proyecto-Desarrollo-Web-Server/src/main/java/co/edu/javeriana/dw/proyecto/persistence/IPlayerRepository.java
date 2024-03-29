package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPlayerRepository extends JpaRepository<Player, Long> {
}
