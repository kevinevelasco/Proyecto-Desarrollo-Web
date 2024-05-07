package co.edu.javeriana.dw.proyecto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "No puede ser vacio")
    private String userName;

    @NotBlank(message = "No puede ser vacio")
    private String password;

    @Enumerated(EnumType.STRING)
    private PlayerType type;

    @JsonIgnore
    @ManyToOne
    private Spacecraft spacecraft;


        // Constructor con todos los campos para pruebas
        public Player(String userName, String password, PlayerType type, Spacecraft spacecraft) {
            this.userName = userName;
            this.password = password;
            this.type = type;
            this.spacecraft = spacecraft;
        }
    

}
