package co.edu.javeriana.dw.proyecto.model;

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
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "El nombre del planeta no puede ser vacio")
    private String name;

    @OneToMany(mappedBy = "planet")
    private List<Spacecraft> spacecrafts;

    @NotNull(message = "La estrella no puede ser nula")
    @ManyToOne
    @JoinColumn(name = "star_id", nullable = false)
    private Star star;

    @ManyToMany
    @JoinTable(
            name = "Market",
            joinColumns = @JoinColumn(name = "planet_id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "product_id", nullable = false)
    )
    private List<Product> products;

    @OneToMany(mappedBy = "planet")
    private List<Market> markets;
}
