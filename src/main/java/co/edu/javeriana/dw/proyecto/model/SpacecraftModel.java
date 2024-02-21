package co.edu.javeriana.dw.proyecto.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SpacecraftModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String modelName;
    private Double storage;
    private Double maxSpeed;

    @OneToOne(mappedBy = "spacecraftModel")
    private Spacecraft spacecraft;
}
