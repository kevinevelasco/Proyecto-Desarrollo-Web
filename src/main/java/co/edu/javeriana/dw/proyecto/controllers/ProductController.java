package co.edu.javeriana.dw.proyecto.controllers;

import co.edu.javeriana.dw.proyecto.model.Player;
import co.edu.javeriana.dw.proyecto.model.Product;
import co.edu.javeriana.dw.proyecto.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.validation.Valid;


import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController{

    Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public String listProducts(Model model) {
        List<Product> products = productService.getAllProduct();
        log.info("Products: " + products.size());
        model.addAttribute("products", products);
        return "product-list";
    }

    @GetMapping("/view/{id}")
    public String viewProduct(Model model, Long  id) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-view";
    }
    @GetMapping("/delete/{id}")
    public String deleteProduct(Model model, Long  id) {
        productService.deleteProduct(id);
        return "redirect:/product/list";
    }
    @GetMapping("/edit/{id}")
    public String editProduct(Model model, @PathVariable Long  id) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-edit";
    }

    @PostMapping(value = "/save")
    public String saveProduct(@Valid Product product, BindingResult result, Model model) {
        if(result.hasErrors()) {
            return "product-edit";
        }
        productService.saveProduct(product);
        return "redirect:/product/list";
    }
}
