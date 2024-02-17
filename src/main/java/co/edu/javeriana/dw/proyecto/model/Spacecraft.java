package co.edu.javeriana.dw.proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Spacecraft {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private Double storage;
    private Double speed;
    private BigDecimal credit;
    private Double time;
    private String coordinates;

    @OneToMany(mappedBy = "spacecraft")
    private List<Player> players;

    @ManyToMany
    @JoinTable(
            name = "Space_Hangar",
            joinColumns = @JoinColumn(name = "spacecraft_id"),
            inverseJoinColumns = @JoinColumn(name = "planet_id")
    )
    private List<Planet> planets;
}
