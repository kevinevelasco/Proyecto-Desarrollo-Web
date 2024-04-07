package co.edu.javeriana.dw.proyecto.auth;

import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.persistence.IInventoryRepository;
import co.edu.javeriana.dw.proyecto.persistence.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private IPlayerRepository playerRepository;

    public Player login(LoginRequest loginRequest) { //todo toca devolver es una respuesta y NO el objeto en si
        Player player = playerRepository.findByUserName(loginRequest.getUsername());
        if (player != null && player.getPassword().equals(loginRequest.getPassword())) {
            return player;
        }
        return new Player();
    }
}
