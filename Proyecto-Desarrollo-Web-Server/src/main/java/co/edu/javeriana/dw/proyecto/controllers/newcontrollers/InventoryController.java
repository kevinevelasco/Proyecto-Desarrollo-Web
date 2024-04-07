package co.edu.javeriana.dw.proyecto.controllers.newcontrollers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.javeriana.dw.proyecto.model.Inventory;
import co.edu.javeriana.dw.proyecto.service.InventoryService;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {

@Autowired
private InventoryService inventoryService;

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
    
}
