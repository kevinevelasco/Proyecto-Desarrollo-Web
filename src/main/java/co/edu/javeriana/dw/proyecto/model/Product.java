package co.edu.javeriana.dw.proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
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

}
