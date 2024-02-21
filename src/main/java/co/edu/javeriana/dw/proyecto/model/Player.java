package co.edu.javeriana.dw.proyecto.model;

import jakarta.persistence.*;
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

    private String userName;
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

    @Override
    public String toString() {
        return "Player{" +
                "id=" + id +
                ", user='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
