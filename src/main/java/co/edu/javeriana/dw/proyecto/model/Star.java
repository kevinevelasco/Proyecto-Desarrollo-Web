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
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String coordinates;

    @OneToMany(mappedBy = "star")
    private List<Planet> planets;

    @ManyToMany
    @JoinTable(
            name = "Wormhole",
            joinColumns = @JoinColumn(name = "star_id"),
            inverseJoinColumns = @JoinColumn(name = "star_id2")
    )
    private List<Star> wormholes;

    @ManyToMany(mappedBy = "wormholes")
    private List<Star> destinations;
}
