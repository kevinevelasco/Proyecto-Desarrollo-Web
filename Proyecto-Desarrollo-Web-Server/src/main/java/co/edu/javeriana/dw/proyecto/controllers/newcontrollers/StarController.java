package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.javeriana.dw.proyecto.model.Star;
import co.edu.javeriana.dw.proyecto.service.StarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import co.edu.javeriana.dw.proyecto.model.Planet;



@RestController
@RequestMapping("/api/star")
public class StarController {

    @Autowired
    private StarService starService;

    // http://localhost:8080/api/star/list
    @GetMapping("/list")
    public List<Star> listStars() {
        return starService.getAllStars();
    }

    @GetMapping("/list-page")
    public Page<Star> getAllStars(Pageable pageable) {
        return starService.listarEstrellasPaginable(pageable);
    }

    // https://www.baeldung.com/spring-rest-openapi-documentation
    @GetMapping("/search")
    public Page<Star> searchStar(@RequestParam String name, Pageable pageable) {
        return starService.buscarEstrella(name, pageable);
    }

    // http://localhost:8080/api/star/2
    @Operation(summary = "Buscar estrellas por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Encontró la estrella", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Star.class)) }),
            @ApiResponse(responseCode = "400", description = "Id suministrado es invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Estrella no encontrada", content = @Content) })

    @GetMapping("/{id}")
    public Star recoverStar(@PathVariable Long id) {
        return starService.getStarById(id);
    }

    @PostMapping("")
    public Star createStar(@RequestBody Star star) {
        return starService.saveStar(star);
    }

    @DeleteMapping("/{id}")
    public void deleteStar(@PathVariable Long id) {
        starService.deleteStar(id);
    }

    @PutMapping("")
    public Star updateStar(@RequestBody Star star) {
        return starService.saveStar(star);
    }

    @PatchMapping("{id}/name")
    public Map<String, Object> modifyName(@PathVariable Long id, @RequestBody String nuevoNombre) {
        int numeroRegistrosModificados = starService.actualizarNombreEstrella(id, nuevoNombre);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("cantidadTuplasModificadas", numeroRegistrosModificados);
        return respuesta;
    }

    //buscar las 10 estrellas mas cercanas
    @GetMapping("/{id}/nearest")
    public List<Star> getNearestStars(@PathVariable Long id) {
        return starService.findNearestStars(id, 10);
    }

    //esto muestra los planetas q hay en la estrella
    @GetMapping("/{id}/planets")
    public List<Planet> getPlanetsByStar(@PathVariable Long id) {
        return starService.findPlanetsByStarId(id);
    }
}
