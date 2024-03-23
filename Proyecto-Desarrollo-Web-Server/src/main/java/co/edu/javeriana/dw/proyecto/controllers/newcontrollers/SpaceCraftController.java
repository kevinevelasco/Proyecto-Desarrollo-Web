package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
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

    @Autowired
    private SpacecraftService spaceCraftService;

    @Autowired
    private SpacecraftModelService spacecraftModelService;

    @Autowired
    private PlanetService planetService;

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

    @PatchMapping("/{id}/name")
    public Map<String, Object> updateSpaceCraftName(@PathVariable Long id, @RequestBody String name) {
        int numeroRegistrosModificados = spaceCraftService.actualizarNombreNave(id, name);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("cantidadTuplasModificadas", numeroRegistrosModificados);
        return respuesta;
    }
}
