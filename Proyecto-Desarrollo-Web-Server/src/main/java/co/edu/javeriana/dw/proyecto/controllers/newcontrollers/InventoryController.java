package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import java.util.List;

import co.edu.javeriana.dw.proyecto.model.Product;
import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.service.ProductService;
import co.edu.javeriana.dw.proyecto.service.SpacecraftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.javeriana.dw.proyecto.model.Inventory;
import co.edu.javeriana.dw.proyecto.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

@Autowired
private InventoryService inventoryService;
@Autowired
private SpacecraftService spacecraftService;
@Autowired
private ProductService productService;

    // http://localhost:8080/api/inventory/get/{spacecraftId}
    @GetMapping("/get/{spacecraftId}")
    public ResponseEntity<List<Inventory>> getInventory(@PathVariable Long spacecraftId) {
        List<Inventory> inventories = inventoryService.getInventoriesBySpacecraft(spacecraftId);
        if(inventories.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } else {
            return ResponseEntity.ok(inventories);
        }
    }

    //obtenemos el almacenamiento total multiplicando la cantidad del producto por su tamaño
    @GetMapping("/get/total/{spacecraftId}")
    public ResponseEntity<Double> getTotalInventory(@PathVariable Long spacecraftId) {
        List<Inventory> inventories = inventoryService.getInventoriesBySpacecraft(spacecraftId);
        double total = 0;
        for (Inventory inventory : inventories) {
            total += inventory.getProduct().getSize() * inventory.getQuantity();
        }
        return ResponseEntity.ok(total);
    }

    //crear inventario en la lista de inventarios de mi spacecraft
    @PostMapping("/create/{spacecraftId}/{productId}")
    public ResponseEntity<Inventory> createInventory(@PathVariable Long spacecraftId, @PathVariable Long productId) {
        Spacecraft spacecraft = spacecraftService.getSpacecraftById(spacecraftId);
        Product product = productService.getProductById(productId);
        Inventory inventory = new Inventory();

        //creamos un id temporal para el inventario
        inventory.setId((long) (spacecraft.getInventories().size() + 1));
        inventory.setSpacecraft(spacecraft);
        inventory.setProduct(product);
        inventory.setQuantity(1);
        spacecraft.getInventories().add(inventory);
        inventoryService.saveInventory(inventory);
        spacecraftService.saveSpacecraft(spacecraft);
        return ResponseEntity.ok(inventory);
    }

    //actualizamos la cantidad de un producto en el inventario si ya existe, en 1
    @PatchMapping("/update/{spacecraftId}/{productId}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Long spacecraftId, @PathVariable Long productId) {
        Spacecraft spacecraft = spacecraftService.getSpacecraftById(spacecraftId);
        Product product = productService.getProductById(productId);
        Inventory inventory = spacecraft.getInventories().stream()
                .filter(i -> i.getProduct().getId().equals(productId))
                .findFirst()
                .orElse(null);
        if (inventory == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        inventory.setQuantity(inventory.getQuantity() + 1);
        inventoryService.saveInventory(inventory);
        return ResponseEntity.ok(inventory);
    }
    
}
