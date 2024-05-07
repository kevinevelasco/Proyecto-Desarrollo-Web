package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.service.PlayerService;
import co.edu.javeriana.dw.proyecto.service.SpacecraftService;
import lombok.Delegate;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    Logger log = LoggerFactory.getLogger(getClass());
    
    @Autowired
    private PlayerService playerService;

    @Autowired
    private SpacecraftService spacecraftService;

    @Autowired
    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }


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

    @DeleteMapping("/{id}")
    public ResponseEntity<Player> deletePlayer(@PathVariable Long id){
        Player player = playerService.getPlayerById(id);
        if(player != null){
            playerService.deletePlayer(id);
            return ResponseEntity.ok(player);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Player> registerPlayer(@RequestBody Player player) {
        // Antes de registrar, verifica si ya existe un jugador con el mismo nombre de usuario
        Player existingPlayer = playerService.getPlayerByUsername(player.getUserName());
        if (existingPlayer != null) {
            return ResponseEntity.badRequest().body(null); // Usuario ya existe
        }
    
        // Si no existe, registra el nuevo jugador
        Player newPlayer = playerService.savePlayer(player);
        return ResponseEntity.ok(newPlayer);
    }




    @PutMapping("/{id}")
    public ResponseEntity<Player> updatePlayer(@PathVariable Long id, @RequestBody Player updatedPlayer) {
    // Busca el jugador por su ID
    Player existingPlayer = playerService.getPlayerById(id);
    
    // Si el jugador no existe, devuelve un error NOT FOUND
    if (existingPlayer == null) {
        return ResponseEntity.notFound().build();
    }
    
    // Actualiza los datos del jugador
    existingPlayer.setUserName(updatedPlayer.getUserName());
    existingPlayer.setPassword(updatedPlayer.getPassword());
    // Puedes actualizar más campos aquí según sea necesario

    // Guarda los cambios
    Player updated = playerService.savePlayer(existingPlayer);

    // Devuelve el jugador actualizado
    return ResponseEntity.ok(updated);
    }





    @PatchMapping("/{id}")
    public ResponseEntity<Player> updatePlayerPartially(@PathVariable Long id, @RequestBody Player updatedPlayer) {
    Player existingPlayer = playerService.getPlayerById(id);
    if (existingPlayer == null) {
        return ResponseEntity.notFound().build();
    }
    if (updatedPlayer.getUserName() != null) {
        existingPlayer.setUserName(updatedPlayer.getUserName());
    }
    if (updatedPlayer.getPassword() != null) {
        existingPlayer.setPassword(updatedPlayer.getPassword());
    }
    Player updated = playerService.savePlayer(existingPlayer);
    return ResponseEntity.ok(updated);
    }



    



}
