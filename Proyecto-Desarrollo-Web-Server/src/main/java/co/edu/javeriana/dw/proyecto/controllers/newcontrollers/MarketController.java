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
import org.springframework.http.HttpStatus;
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
    private SpacecraftModelService spacecraftModelService;
    @Autowired
    public MarketController(MarketService marketService) {
        this.marketService = marketService;
    }

    Logger log = LoggerFactory.getLogger(getClass());

    //localhost:8080/api/market/list
    @GetMapping("/list")
    public List<Market> listMarkets() {
        return marketService.getAllMarket();
    }

    // Constructor para inyectar dependencias en pruebas
    public MarketController(SpacecraftService spacecraftService) {
        this.spacecraftService = spacecraftService;
    }


    //localhost:8080/api/market/venta/1/1
    //esta prueba sirve para probar la venta de productos
    @PatchMapping("/{toDo}/{id}/{quantity}")
    public Spacecraft sellProduct(@PathVariable String toDo, @PathVariable Long id, @PathVariable Double quantity){
        Spacecraft spacecraft = spacecraftService.getSpacecraftById(id);
        // Convertir Long a BigDecimal
        BigDecimal quantityBD = BigDecimal.valueOf(quantity);
        // Realizar la operación
        if(toDo.equals("substract")){
            spacecraft.setCredit(spacecraft.getCredit().subtract(quantityBD));
        } else {
            spacecraft.setCredit(spacecraft.getCredit().add(quantityBD));
        }
        spacecraftService.saveSpacecraft(spacecraft);
        return spacecraft;
    }

    //esto es para reducir el stock del mercado
    //localhost:8080/api/market/venta/1/product/1/planet/1/
    //localhost:8080/api/market/1/sell
    @PatchMapping("/{id}/{toDo}")
    public Market sellProductStock(@PathVariable Long id, @PathVariable String toDo){

        Market market = marketService.getMarketById(id);
        if(toDo.equals("sell")){
            System.out.println("a la tupla con id " + id + " se le va a restar 1 al stock");
            if(market.getStock() == 0){
                return market;
            } else {
                market.setStock(market.getStock() - 1);
            }
        } else {
            System.out.println("a la tupla con id " + id + " se le va a sumar 1 al stock");
            market.setStock(market.getStock() + 1);
        }
        return marketService.saveMarket(market);
    }

    //localhost:8080/api/market/list-page
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

    //localhost:8080/api/market/1     en donde esta el 1 se pone el numero del id del mercado que se quiere traer
    @GetMapping("/{id}")
    public Market getMarket(@PathVariable Long id) {
        return marketService.getMarketById(id);
    }

    //localhost:8080/api/market/planet/1     en donde esta el 1 se pone el numero del id del mercado que se quiere traer
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

    //link para probar el metodo post localhost:8080/api/market/1/1
    @PostMapping("/{productId}/{planetId}")
    public Market saveMarket(@PathVariable Long productId, @PathVariable Long planetId, @RequestBody Market market) {
        Product product = productService.getProductById(productId);
        Planet planet = planetService.getPlanetById(planetId);
        market.setProduct(product);
        market.setPlanet(planet);
        log.info("Market: " + market);
        return marketService.saveMarket(market);
    }

    //localhost:8080/api/market/1   en donde esta el 1 se pone el numero del id del mercado que se quiere borrar
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMarket(@PathVariable Long id) {
        Market market = marketService.getMarketById(id);
        if(market != null){
            marketService.deleteMarket(id);
            return ResponseEntity.ok(market);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    //localhost:8080/api/market/1
    @PutMapping("")
    public Market updateMarket(@RequestBody Market market) {
        if(market.getPlanet() == null || market.getProduct() == null){ //si recibe planet o product nulos es porque quiere que estos queden igual
            Market marketAux = marketService.getMarketById(market.getId());
            market.setPlanet(marketAux.getPlanet());
            market.setProduct(marketAux.getProduct());
        }
        return marketService.saveMarket(market);
    }

    //link para probar el metodo post localhost:8080/api/market/create/1/1
    @PostMapping("/create/{planetId}/{productId}")
    public ResponseEntity<Market> createMarket(@PathVariable Long planetId, @PathVariable Long productId, @RequestBody Market market) {

        Planet planet = planetService.getPlanetById(planetId);
        Product product = productService.getProductById(productId);

        //creamos un id temporal para el mercado
        market.setId((long) (marketService.getAllMarket().size() + 1));
        market.setStock(1);
        market.setPlanet(planet);
        market.setProduct(product);

        return ResponseEntity.ok(marketService.saveMarket(market));
    }


//para probar que este controller funciona lo hago desde postman, pongo put y esta url "localhost:8080/api/market/2/update"
//ademas toca ir a body, en json y poner los datos q uno quiere q cambien
/*esto es un ejemplo de lo q se puede poner
{ 
    "id": 1,
    "stock": 20000000,
    "demandFactor": 5,
    "supplyFactor": 8,
    "buyPrice": 100.0,
    "sellPrice": 150.0,
    "planet": {
        "id": 1
    },
    "product": {
        "id": 1
    }
}*/

    @PutMapping("/{id}/update")
    public ResponseEntity<Market> updateMarket(@PathVariable Long id, @RequestBody Market updatedMarket) {
    Market existingMarket = marketService.getMarketById(id);

    if (existingMarket == null) {
        return ResponseEntity.notFound().build();
    }

    if (updatedMarket.getPlanet() == null) {
        updatedMarket.setPlanet(existingMarket.getPlanet());
    }

    if (updatedMarket.getProduct() == null) {
        updatedMarket.setProduct(existingMarket.getProduct());
    }

    existingMarket.setStock(updatedMarket.getStock());
    existingMarket.setDemandFactor(updatedMarket.getDemandFactor());
    existingMarket.setSupplyFactor(updatedMarket.getSupplyFactor());
    existingMarket.setBuyPrice(updatedMarket.getBuyPrice());
    existingMarket.setSellPrice(updatedMarket.getSellPrice());
    existingMarket.setPlanet(updatedMarket.getPlanet());
    existingMarket.setProduct(updatedMarket.getProduct());

    return ResponseEntity.ok(marketService.saveMarket(existingMarket));
}






}