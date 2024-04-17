package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import co.edu.javeriana.dw.proyecto.model.Inventory;
import co.edu.javeriana.dw.proyecto.model.Market;
import co.edu.javeriana.dw.proyecto.model.Planet;
import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.model.Product;
import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.model.SpacecraftModel;
import co.edu.javeriana.dw.proyecto.service.MarketService;
import co.edu.javeriana.dw.proyecto.service.PlanetService;
import co.edu.javeriana.dw.proyecto.service.ProductService;
import co.edu.javeriana.dw.proyecto.service.PlayerService;
import co.edu.javeriana.dw.proyecto.service.SpacecraftService;
import co.edu.javeriana.dw.proyecto.service.SpacecraftModelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import net.datafaker.providers.base.Space;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.atomic.AtomicReference;

import java.math.BigDecimal;
import java.util.List;

import javax.swing.Spring;

@RestController
@RequestMapping("/api/market")
public class MarketController {

    @Autowired
    private MarketService marketService;
    @Autowired
    private ProductService productService;
    @Autowired
    private PlanetService planetService;
    @Autowired
    private PlayerService playerService;
    @Autowired
    private SpacecraftService spacecraftService;
    @Autowired
    private SpacecraftModelService spacecraftModelService;  // Asegúrate de tener esta línea en tu controlador
    

    Logger log = LoggerFactory.getLogger(getClass());


    @GetMapping("/list")
    public List<Market> listMarkets() {
        return marketService.getAllMarket();
    }

    
    //localhost:8080/api/market/venta/1/1
    @PatchMapping("/venta/{id}/{quantity}")
    public Spacecraft sellProduct(@PathVariable Long id, @PathVariable Double quantity){
        Spacecraft spacecraft = spacecraftService.getSpacecraftById(id);
        // Convertir Long a BigDecimal
        BigDecimal quantityBD = BigDecimal.valueOf(quantity);
        // Realizar la resta
        spacecraft.setCredit(spacecraft.getCredit().subtract(quantityBD));
        spacecraftService.saveSpacecraft(spacecraft);
        return spacecraft;
    }

    //esto es para reducir el stock del mercado
    //localhost:8080/api/market/venta/1/product/1/planet/1/
    @PatchMapping("/{id}/venta")
    public Market sellProductStock(@PathVariable Long id){
        System.out.println("a la tupla con id " + id + " se le va a restar 1 al stock");
        Market market = marketService.getMarketById(id);
        market.setStock(market.getStock() - 1);
        return marketService.saveMarket(market);
    }

    @GetMapping("/list-page")
    public Page<Market> getAllMarkets(Pageable pageable){
        return marketService.listarMercadosPaginable(pageable);
    }

    @GetMapping("/search")
    public Page<Market> searchMarketsByPlanetId(@RequestParam Long planetId, Pageable pageable){
        return marketService.buscarMercado(planetId, pageable);
    }

    @Operation(summary = "Get market by id", description = "Get market by id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Encontró el mercado",
                            content = { @Content(mediaType = "application/json") }),
                    @ApiResponse(responseCode = "400", description = "Id suministrado es invalido", content = @io.swagger.v3.oas.annotations.media.Content),
                    @ApiResponse(responseCode = "404", description = "Mercado no encontrado")
            }
    )
    @GetMapping("/{id}")
    public Market getMarket(@PathVariable Long id) {
        return marketService.getMarketById(id);
    }
    //Traer market por el id del planeta
    @GetMapping("/planet/{id}")
    public ResponseEntity<List<Market>> getMarketByPlanetId(@PathVariable Long id) {
        List<Market> markets = marketService.getMarketsByPlanetId(id);
        if(markets.isEmpty()){
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(markets);
        }
    }


    @PostMapping("/{productId}/{planetId}")
    public Market saveMarket(@PathVariable Long productId, @PathVariable Long planetId, @RequestBody Market market) {
        Product product = productService.getProductById(productId);
        Planet planet = planetService.getPlanetById(planetId);
        market.setProduct(product);
        market.setPlanet(planet);
        log.info("Market: " + market);
        return marketService.saveMarket(market);
    }


    @DeleteMapping("/{id}")
    public void deleteMarket(@PathVariable Long id) {
        marketService.deleteMarket(id);
    }

    @PutMapping("")
    public Market updateMarket(@RequestBody Market market) {
        if(market.getPlanet() == null || market.getProduct() == null){ //si recibe planet o product nulos es porque quiere que estos queden igual
            Market marketAux = marketService.getMarketById(market.getId());
            market.setPlanet(marketAux.getPlanet());
            market.setProduct(marketAux.getProduct());
        }
        return marketService.saveMarket(market);
    }

}