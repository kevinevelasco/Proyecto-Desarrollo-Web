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
public class Planet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @ManyToMany
    @JoinTable(
            name = "Space_Hangar",
            joinColumns = @JoinColumn(name = "planet_id"),
            inverseJoinColumns = @JoinColumn(name = "spacecraft_id")
    )
    private List<Spacecraft> spacecrafts;

    @ManyToOne
    private Star star;

   @ManyToMany
   @JoinTable(
           name = "Market",
           joinColumns = @JoinColumn(name = "planet_id"),
           inverseJoinColumns = @JoinColumn(name = "product_id")
   )
   private List<Product> products;

   @OneToMany(mappedBy = "planet")
    private List<Market> markets;
}