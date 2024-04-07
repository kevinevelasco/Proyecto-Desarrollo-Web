package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Inventory;
import co.edu.javeriana.dw.proyecto.persistence.IInventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventoryService {

    @Autowired
    private IInventoryRepository inventoryRepository;

    public List<Inventory> getAllInventories() {return inventoryRepository.findAll();}

    public Inventory getInventoryById(Long id) {return inventoryRepository.findById(id).orElse(null);}

    public Inventory saveInventory(Inventory inventory) {return inventoryRepository.save(inventory);}

    public void deleteInventory(Long id) {inventoryRepository.deleteById(id);}


    public List<Inventory> getInventoriesBySpacecraft(Long id) {
        return inventoryRepository.findAllBySpacecraftId(id);
    }
}
