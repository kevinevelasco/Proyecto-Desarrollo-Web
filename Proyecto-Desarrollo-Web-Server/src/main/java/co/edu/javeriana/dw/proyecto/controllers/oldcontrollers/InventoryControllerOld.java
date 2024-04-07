package co.edu.javeriana.dw.proyecto.controllers.oldcontrollers;

import co.edu.javeriana.dw.proyecto.model.Inventory;
import co.edu.javeriana.dw.proyecto.model.Product;
import co.edu.javeriana.dw.proyecto.model.Spacecraft;
import co.edu.javeriana.dw.proyecto.service.InventoryService;
import co.edu.javeriana.dw.proyecto.service.ProductService;
import co.edu.javeriana.dw.proyecto.service.SpacecraftService;
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
public class InventoryControllerOld {

    Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private SpacecraftService spacecraftService;

    @Autowired
    private ProductService productService;


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
            List<Spacecraft> spacecrafts = spacecraftService.getAllSpacecrafts();
            List<Product> products = productService.getAllProduct();
            model.addAttribute("inventory", inventory);
            model.addAttribute("spacecrafts", spacecrafts);
            model.addAttribute("products", products);
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
        List<Spacecraft> spacecrafts = spacecraftService.getAllSpacecrafts();
        List<Product> products = productService.getAllProduct();
        model.addAttribute("inventory", inventory);
        model.addAttribute("spacecrafts", spacecrafts);
        model.addAttribute("products", products);

        return "inventory-edit";
    }
    @GetMapping("/create")
    public String createInventory(Model model) {
        List<Spacecraft> spacecrafts = spacecraftService.getAllSpacecrafts();
        List<Product> products = productService.getAllProduct();
        model.addAttribute("spacecrafts", spacecrafts);
        model.addAttribute("products", products);
        model.addAttribute("inventory", new Inventory());
        return "inventory-create";
    }


}
