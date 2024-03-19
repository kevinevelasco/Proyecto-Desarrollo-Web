package co.edu.javeriana.dw.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import jakarta.validation.constraints.NotNull;

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

    @NotNull(message = "El tamaño no puede estar vacio")
    @Column(nullable = false)
    private Double size;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del producto no puede ser vacio")
    private String name;

    @JsonIgnore
    @ManyToMany(mappedBy = "products")
    private List<Planet> planets;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Market> markets;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<Inventory> inventories;
}
