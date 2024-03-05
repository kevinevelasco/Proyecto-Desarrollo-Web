package co.edu.javeriana.dw.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import jakarta.validation.constraints.NotNull;

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

    @Column(name = "star_name",nullable = false)
    @NotBlank(message = "El nombre de la estrella no puede ser vacio")
    private String name;

    @NotNull(message = "La coordenada x no puede estar vacia")
    @Column(nullable = false)
    private Double x;

    @NotNull(message = "La coordenada y no puede estar vacia")
    @Column(nullable = false)
    private Double y;

    @NotNull(message = "La coordenada < no puede estar vacia")
    @Column(nullable = false)
    private Double z;

    @OneToMany(mappedBy = "star")
    private List<Planet> planets = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "Wormhole",
            joinColumns = @JoinColumn(name = "star_id"),
            inverseJoinColumns = @JoinColumn(name = "star_id2")
    )
    private List<Star> wormholes;

    @ManyToMany(mappedBy = "wormholes")
    private List<Star> destinations;
}
