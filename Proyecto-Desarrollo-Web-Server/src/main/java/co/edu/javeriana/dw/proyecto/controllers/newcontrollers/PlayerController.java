package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.service.PlayerService;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private PlayerService playerService;


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
    

}
