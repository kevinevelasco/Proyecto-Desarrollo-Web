package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import co.edu.javeriana.dw.proyecto.model.Inventory;
import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.model.PlayerDTO;
import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import co.edu.javeriana.dw.proyecto.persistence.ISpacecraftRepository;
import co.edu.javeriana.dw.proyecto.service.PlanetService;
import co.edu.javeriana.dw.proyecto.service.SpacecraftModelService;
import co.edu.javeriana.dw.proyecto.service.SpacecraftService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/spacecraft")
public class SpaceCraftController {

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpacecraftService spaceCraftService;

    @Autowired
    private SpacecraftModelService spacecraftModelService;

    @Autowired
    private PlanetService planetService;
    
    @Autowired
    private ISpacecraftRepository spaceCraftRepository;

    // http://localhost:8080/api/spacecraft/list
    @GetMapping("/list")
    public List< Spacecraft> listSpaceCrafts() {
        return spaceCraftService.getAllSpacecrafts();
    }

    @GetMapping("/list-page")
    public Page<Spacecraft> getAllSpacecrafts(Pageable pageable) {
        return spaceCraftService.listarNavesPaginable(pageable);
    }

    @GetMapping("/search")
    public Page<Spacecraft> searchSpaceCraft(@RequestParam String name, Pageable pageable) {
        System.out.println(spaceCraftService.buscarSpacecraft(name,pageable));
        return spaceCraftService.buscarSpacecraft(name, pageable);
    }

    @Operation(summary = "Buscar nave espacial por id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Encontró la nave espacial", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Spacecraft.class)) }),
            @ApiResponse(responseCode = "400", description = "Id suministrado es invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Nave espacial no encontrada", content = @Content) })
    @GetMapping("/{id}")
    public Spacecraft  recoverSpaceCraft(@PathVariable Long id) {
        return spaceCraftService.getSpacecraftById(id);
    }

//    @PostMapping("")
//    public Spacecraft createSpaceCraft(@RequestBody Spacecraft spacecraft) {
//        return spaceCraftService.saveSpacecraft(spacecraft);
//    }

    @PostMapping("/{modelId}/{planetId}") //método POST para crear una nave espacial asociada a un modelo de nave espacial
    public Spacecraft createSpacecraft(@PathVariable Long modelId, @PathVariable Long planetId, @RequestBody Spacecraft spacecraft) {
        SpacecraftModel model = spacecraftModelService.getSpacecraftModelById(modelId);
        Planet planet = planetService.getPlanetById(planetId);
        spacecraft.setSpacecraftModel(model);
        spacecraft.setPlanet(planet);
        return spaceCraftService.saveSpacecraft(spacecraft);
    }

    @DeleteMapping("/{id}")
    public void deleteSpaceCraft(@PathVariable Long id) {
        spaceCraftService.deleteSpacecraft(id);
    }

    @PutMapping("")
    public Spacecraft updateSpaceCraft(@Valid @RequestBody Spacecraft spacecraft) {
        return spaceCraftService.saveSpacecraft(spacecraft);
    }
 //hacemos un método para actualizar el planeta en el que se encuentra la nave, usando el id del jugador y el id del planeta
    //http://localhost:8080/api/spacecraft/player/1/planet/1
    //MÉTODO REALIZADO UNICAMENTE POR EL ROL DE PILOTO
    @Secured({ "PILOT", "CAPTAIN" })
    @PatchMapping("/player/{id}/planet/{planetId}")
    public Spacecraft updateSpaceCraftPlanet(@PathVariable Long id, @PathVariable Long planetId) {
        //filtramos en las naves, en las que tenga ese jugador con id = id
        //obtenemos la spacecraft que contiene en su lista de jugadores el jugador con el id {id}
        Spacecraft spacecraft = spaceCraftRepository.findAll().stream()
                .filter(s -> s.getPlayers().stream().anyMatch(p -> p.getId().equals(id)))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Nave no encontrada"));
        spacecraft.setPlanet(planetService.getPlanetById(planetId));
        spaceCraftService.saveSpacecraft(spacecraft);
        return spacecraft;
    }


    @PatchMapping("/{id}/name")
    public Map<String, Object> updateSpaceCraftName(@PathVariable Long id, @RequestBody String name) {
        int numeroRegistrosModificados = spaceCraftService.actualizarNombreNave(id, name);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("cantidadTuplasModificadas", numeroRegistrosModificados);
        return respuesta;
    }

    @GetMapping("{id}/model")
    public SpacecraftModel getModel(@PathVariable Long id) {
        return spaceCraftService.getSpacecraftById(id).getSpacecraftModel();
    }

    @GetMapping("{id}/planet")
    public Planet getPlanet(@PathVariable Long id) {
        log.info("Planeta de la nave con id: " + id+ " es: " + spaceCraftService.getSpacecraftById(id).getPlanet());
        return spaceCraftService.getSpacecraftById(id).getPlanet();
    }

    //hacemos un get de la carga actual de productos de la nave
    @GetMapping("{id}/actualInventory")
    public Double getInventory(@PathVariable Long id) {
        var inventories = spaceCraftService.getSpacecraftById(id).getInventories();
        var actualInventory = 0.0;
        //la capacidad es igual al total de cada producto por su tamaño
        for (Inventory inventory : inventories) {
            actualInventory += inventory.getProduct().getSize() * inventory.getQuantity();
        }
        return actualInventory;
    }

    //Hacemos un get de los productos que tiene la nave
    @GetMapping("{id}/inventory")
    public ResponseEntity<List<Inventory>> getInventoryList(@PathVariable Long id) {
        List<Inventory> inventories= spaceCraftService.getSpacecraftById(id).getInventories();
        if(inventories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(inventories);
        }
    }
    //hacemos un set en la base de datos del nuevo planeta en el cuál se encuentra la nave, el cual será uno de los que tenga la star que le llega

    //Todas las spacecraft que estan en el mismo planetid
    @GetMapping("/{planetId}/spacecrafts")
    public ResponseEntity<List<Spacecraft>> getSpacecraftsByPlanetId(@PathVariable Long planetId) {
        List<Spacecraft> spacecrafts = spaceCraftService.getSpacecraftsByPlanetId(planetId);
        //convertimos los players de spacecrafts en PlayerDTOs
        for (Spacecraft spacecraft : spacecrafts) {
            //eliminamos los atributos innecesarios, solo dejamos el id, username, password y type de cada jugador de la lista de players
            List<Player> players = spacecraft.getPlayers().stream().map(player -> new Player(player.getId(), player.getUsername(), player.getPassword(), player.getType(), player.getSpacecraft())).collect(Collectors.toList());
            spacecraft.setPlayers(players);
        }

        if(spacecrafts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            log.info("Naves encontradas: " + spacecrafts.toString());
            return ResponseEntity.ok(spacecrafts);
        }
    }

    @GetMapping("/{spacecraftId}/players")
    public ResponseEntity<List<PlayerDTO>> getPlayersBySpacecraft(@PathVariable Long spacecraftId) {
        Spacecraft spacecraft = spaceCraftService.getSpacecraftById(spacecraftId);
        if(spacecraft != null && spacecraft.getPlayers() != null) {
            List<PlayerDTO> players = spacecraft.getPlayers().stream().map(player -> new PlayerDTO(player.getId(), player.getUsername())).collect(Collectors.toList());
            System.out.println("Jugadores encontrados: ");
            players.forEach(System.out::println);
            return ResponseEntity.ok(players);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PatchMapping("/{spacecraftId}/time/{totalTime}")
    public ResponseEntity<Spacecraft> updateSpacecraftTime(@PathVariable Long spacecraftId, @PathVariable Long totalTime) {
        Spacecraft spacecraft = spaceCraftService.getSpacecraftById(spacecraftId);
        if(spacecraft != null) {
            spacecraft.setTotalTime(totalTime.doubleValue());
            log.info("Tiempo a actualizar: " + totalTime.doubleValue());
            log.info("Tiempo en nave actualizado: " + spacecraft.getTotalTime());
            spaceCraftService.saveSpacecraft(spacecraft);
            return ResponseEntity.ok(spaceCraftService.saveSpacecraft(spacecraft));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    
    
}
