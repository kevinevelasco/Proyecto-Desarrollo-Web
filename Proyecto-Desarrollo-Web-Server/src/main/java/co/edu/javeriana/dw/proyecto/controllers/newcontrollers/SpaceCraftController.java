package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import co.edu.javeriana.dw.proyecto.model.Inventory;
import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import co.edu.javeriana.dw.proyecto.persistence.ISpacecraftRepository;
import co.edu.javeriana.dw.proyecto.service.PlanetService;
import co.edu.javeriana.dw.proyecto.service.PlayerService;
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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private PlayerService playerService;

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
    public List<Inventory> getInventoryList(@PathVariable Long id) {
        return spaceCraftService.getSpacecraftById(id).getInventories();
    }
    //hacemos un set en la base de datos del nuevo planeta en el cuál se encuentra la nave, el cual será uno de los que tenga la star que le llega
}
