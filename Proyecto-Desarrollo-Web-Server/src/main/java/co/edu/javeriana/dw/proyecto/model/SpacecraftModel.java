package co.edu.javeriana.dw.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SpacecraftModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "No puede ser vacio")
    @NotNull(message = "No puede ser vacio")
    //@Size(min = 5, max = 20, message = "Model name must be between 5 and 20 characters")
    private String modelName;

    @NotNull(message = "No puede ser vacio")
    @Positive(message = "Storage should not be less than 1 m^3")
    private Double storage;

    @NotNull(message = "No puede ser vacio")
    @Positive(message = "Speed should not be less than 1 km/s")
    private Double maxSpeed;

    @JsonIgnore
    @OneToMany(mappedBy = "spacecraftModel")
    private List<Spacecraft> spacecrafts;
}
