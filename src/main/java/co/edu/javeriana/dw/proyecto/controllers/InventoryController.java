package co.edu.javeriana.dw.proyecto.controllers;

import co.edu.javeriana.dw.proyecto.model.Inventory;
import co.edu.javeriana.dw.proyecto.service.InventoryService;
import jakarta.validation.Valid;
import org.slf4j.Logger;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;


@Controller
@RequestMapping("/inventory")
public class InventoryController {

    Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private InventoryService inventoryService;

    @GetMapping("/list")
    public String listInventories(Model model) {
        List<Inventory> inventories = inventoryService.getAllInventories();
        log.info("Inventories: " + inventories.size());
        model.addAttribute("inventories", inventories);
        return "inventory-list";
    }

    @GetMapping("/view/{id}")
    public String viewInventory(Model model,  @PathVariable Long  id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        model.addAttribute("inventory", inventory);
        return "inventory-view";
    }

    @PostMapping(value = "/save")
    public String saveInventory(@Valid Inventory inventory, BindingResult result, Model model) {
        if(result.hasErrors()) {
            model.addAttribute("inventory", inventory);
            return "inventory-edit";
        }
        log.info("Inventory: " + inventory.toString());
        inventoryService.saveInventory(inventory);
        return "redirect:/inventory/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteInventory(@PathVariable Long  id) {
        inventoryService.deleteInventory(id);
        return "redirect:/inventory/list";
    }

    @GetMapping("/edit/{id}")
    public String editInventory(Model model, @PathVariable Long id) {
        Inventory inventory = inventoryService.getInventoryById(id);
        model.addAttribute("inventory", inventory);
        return "inventory-edit";
    }


}
