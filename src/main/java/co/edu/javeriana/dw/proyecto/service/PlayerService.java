package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.persistence.IPlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private IPlayerRepository playerRepository;

    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElse(null);
    }
    public Player savePlayer(Player player) {
        return playerRepository.save(player);
    }
    public void deletePlayer(Long id) {
        playerRepository.deleteById(id);
    }


}
