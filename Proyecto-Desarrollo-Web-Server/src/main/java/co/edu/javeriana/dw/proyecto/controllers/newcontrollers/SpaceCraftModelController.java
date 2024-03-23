package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
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
@RequestMapping("/api/spacecraft-model")
public class SpaceCraftModelController {
    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private SpacecraftModelService spacecraftModelService;

    @Autowired
    private SpacecraftService spacecraftService;

    @GetMapping("/list")
    public List<SpacecraftModel> listSpaceCraftModels() {
        return spacecraftModelService.getAllSpacecraftModels();
    }

    @GetMapping("/list-page")
    public Page<SpacecraftModel> getAllSpacecraftModels(Pageable pageable) {
        return spacecraftModelService.listarModelosNavesPaginable(pageable);
    }

    @GetMapping("/search")
    public Page<SpacecraftModel> searchSpaceCraftModel(@RequestParam String modelName, Pageable pageable) {
        return spacecraftModelService.buscarSpacecraftModel(modelName, pageable);
    }

    @Operation(summary = "Get spacecraft model by id", description = "Get spacecraft model by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Encontró el modelo de nave espacial",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SpacecraftModel.class)) }),
            @ApiResponse(responseCode = "400", description = "Id suministrado es invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Modelo de nave espacial no encontrada") })
    @GetMapping("/{id}")
    public SpacecraftModel getSpaceCraftModel(@PathVariable Long id) {
        return spacecraftModelService.getSpacecraftModelById(id);
    }

    @PostMapping("")
    public SpacecraftModel saveSpaceCraftModel(@Valid @RequestBody SpacecraftModel model) {
        return spacecraftModelService.saveSpacecraftModel(model);
    }


    @DeleteMapping("/{id}")
    public void deleteSpaceCraftModel(@PathVariable Long id) {
        spacecraftModelService.deleteSpacecraftModel(id);
    }

    @PutMapping("")
    public SpacecraftModel updateSpaceCraftModel(@Valid @RequestBody SpacecraftModel model) {
        return spacecraftModelService.saveSpacecraftModel(model);
    }

    @PatchMapping("/{id}/modelName")
    public Map<String, Object> updateSpaceCraftModelName(@PathVariable Long id, @RequestBody String modelName) {
        int numeroRegistrosModificados = spacecraftModelService.actualizarNombreModeloNave(id, modelName);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("cantidadTuplasModificadas", numeroRegistrosModificados);
        return respuesta;
    }

}
