package co.edu.javeriana.dw.proyecto.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
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
    private List<Planet> planets = new ArrayList<>();

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
