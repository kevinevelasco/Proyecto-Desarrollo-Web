package co.edu.javeriana.dw.proyecto.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Spacecraft {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private BigDecimal credit;
    private Double totalTime;
    private String coordinates; //TODO falta agregarlo al diagrama lógico

    @OneToMany(mappedBy = "spacecraft")
    private List<Player> players;

    @ManyToMany
    @JoinTable(
            name = "Space_Hangar",
            joinColumns = @JoinColumn(name = "spacecraft_id"),
            inverseJoinColumns = @JoinColumn(name = "planet_id")
    )
    private List<Planet> planets;

    @OneToOne
    private SpacecraftModel spacecraftModel;

    @OneToMany(mappedBy = "spacecraft")
    private List<Inventory> inventories;
}
