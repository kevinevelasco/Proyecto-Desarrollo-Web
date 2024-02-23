package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Inventory;
import co.edu.javeriana.dw.proyecto.model.Market;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInventoryRepository extends JpaRepository<Inventory, Long> {
}
