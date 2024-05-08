package co.edu.javeriana.dw.proyecto.auth;

import co.edu.javeriana.dw.proyecto.model.PlayerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    String token;
    private String username;
    private PlayerType role;
}
