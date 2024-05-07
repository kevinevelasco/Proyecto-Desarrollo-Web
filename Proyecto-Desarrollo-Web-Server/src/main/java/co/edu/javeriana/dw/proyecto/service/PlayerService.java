package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.persistence.IPlayerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private Player loggedInPlayer;

    public Player getLoggedInPlayer() {
        return loggedInPlayer;
    }

    @Autowired
    private IPlayerRepository playerRepository;

    Logger log = LoggerFactory.getLogger(getClass());

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


    public String login(String user, String password) {
        Player player = playerRepository.findByUserName(user);
        if(player == null) {//No existe el usuario
            return "No existe el usuario";
        }else if(player.getPassword().equals(password)) {
            loggedInPlayer = player;
            return "Inicio de sesion exitoso";
        }else
        return "Contraseña incorrecta";
    }

    public Player getPlayerByUsername(String username) {
        return playerRepository.findByUserName(username);
    }




}
