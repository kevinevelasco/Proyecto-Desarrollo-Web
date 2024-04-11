package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.service.PlayerService;
import co.edu.javeriana.dw.proyecto.service.SpacecraftService;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private PlayerService playerService;

    @Autowired
    private SpacecraftService spacecraftService;


    @PostMapping("/login")
    public ResponseEntity<Player> login( @RequestBody Player player){
        String msg = playerService.login(player.getUserName(), player.getPassword());
        if(msg.equals("Inicio de sesion exitoso")){
            Player loggedInPlayer = playerService.getLoggedInPlayer();
            return ResponseEntity.ok(loggedInPlayer);
        }else if(msg.equals("No existe el usuario")){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    @GetMapping("/loggedInPlayer")
    public ResponseEntity<Player> getLoggedInPlayer(){
        Player player = playerService.getLoggedInPlayer();
        if(player != null){
            return ResponseEntity.ok(player);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Player> getPlayerById(@PathVariable Long id){
        Player player = playerService.getPlayerById(id);
        if(player != null){
            log.info("Player found: " + player.toString());
            return ResponseEntity.ok(player);
        }else{
            log.info("Player not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    @GetMapping("/{id}/spacecraft")
    public ResponseEntity<Spacecraft> getPlayerSpacecraft(@PathVariable Long id){
        Player player = playerService.getPlayerById(id);
        Spacecraft spacecraft = spacecraftService.getSpacecraftById(player.getSpacecraft().getId());
        if(spacecraft != null){
            log.info("Spacecraft found: " + spacecraft.getName().toString());
            return ResponseEntity.ok(spacecraft);
        }else{
            log.info("Spacecraft not found");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    
    

}
