package co.edu.javeriana.dw.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(of = "id")
public class Spacecraft {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "No puede ser vacio")
    @NotNull(message = "No puede ser vacio")
    //@Size(min = 1, max = 20, message = "Spacecraft name must be between 5 and 20 characters")
    private String name;

    @NotNull(message = "No puede ser vacio")
    @Positive(message = "Credit should not be less than 1")
    private BigDecimal credit;

    @NotNull(message = "No puede ser vacio")
    @Positive(message = "Total time should not be less than 1")
    private Double totalTime;

    @JsonIgnore
    @OneToMany(mappedBy = "spacecraft")
    private List<Player> players;

    @JsonIgnore
    @ManyToOne
    //@NotNull(message = "No puede ser vacio") TODO revisar por qué al quitarlo da error en batch
    @JoinColumn(name = "planet_id")
    private Planet planet;

    @JsonIgnore
    @ManyToOne
    @NotNull(message = "No puede ser vacio")
    private SpacecraftModel spacecraftModel;

    @JsonIgnore
    @OneToMany(mappedBy = "spacecraft")
    private List<Inventory> inventories;

}
