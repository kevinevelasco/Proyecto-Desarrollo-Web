package co.edu.javeriana.dw.proyecto.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Double size;
    private String name;
    @ManyToMany(mappedBy = "products")
    private List<Planet> planets;

    @OneToMany(mappedBy = "product")
    private List<Market> markets;

    @OneToMany(mappedBy = "product")
    private List<Inventory> inventories;

}
