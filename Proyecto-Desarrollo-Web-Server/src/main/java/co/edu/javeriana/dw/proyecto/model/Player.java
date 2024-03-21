package co.edu.javeriana.dw.proyecto.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(name = "user_name", nullable = false)
    @NotBlank(message = "No puede ser vacio")
    private String userName;

    @Column(name = "password", nullable = false)
    @NotBlank(message = "No puede ser vacio")
    private String password;
    @Enumerated(EnumType.STRING)
    private PlayerType type;
    @ManyToOne
    private Spacecraft spacecraft;

    public Player(String user, String password, PlayerType type) {
        this.userName = user;
        this.password = password;
        this.type = type;
    }

}
