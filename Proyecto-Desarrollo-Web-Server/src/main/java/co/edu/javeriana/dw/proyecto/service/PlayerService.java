package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.persistence.IPlayerRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) {
                System.out.println("Username: " + username);
                return playerRepository.findByUserName(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }
    public Player getPlayerByUsername(String username) {
        return playerRepository.findByUserName(username)
        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}
