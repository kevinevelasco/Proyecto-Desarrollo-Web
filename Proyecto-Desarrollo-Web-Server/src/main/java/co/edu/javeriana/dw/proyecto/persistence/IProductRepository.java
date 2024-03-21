package co.edu.javeriana.dw.proyecto.persistence;

import co.edu.javeriana.dw.proyecto.model.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IProductRepository extends JpaRepository<Product, Long> {
    @Transactional
    @Modifying
    @Query("UPDATE Product s SET s.name = :name WHERE s.id = :id")
    int updateProductName(@Param("id") Long id, @Param("name") String name);

    Page<Product> findAllByNameStartingWithIgnoreCase(String name, Pageable pageable);
}
