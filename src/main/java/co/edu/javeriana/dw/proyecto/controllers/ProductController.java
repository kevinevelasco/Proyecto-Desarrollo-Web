package co.edu.javeriana.dw.proyecto.controllers;

import co.edu.javeriana.dw.proyecto.model.Product;
import co.edu.javeriana.dw.proyecto.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


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
    public String viewProduct(Model model, @PathVariable Long  id) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "product-view";
    }
    @GetMapping("/delete/{id}")
    public String deleteProduct(Model model,@PathVariable Long  id) {
        productService.deleteProduct(id);
        return "redirect:/product/list";
    }
    @GetMapping("/edit/{id}")
    public String editProduct(@PathVariable Long  id, Model model) {
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

    @GetMapping("/search")
    public String listProducts(@RequestParam(required = false) String searchText, Model model) {
        List<Product> products;
        if (searchText == null || searchText.trim().equals("")) {
            log.info("No hay texto de búsqueda. Retornando todo");
            products = productService.getAllProduct();
        } else {
            log.info("Buscando estrellas cuyo nombre comienza con {}", searchText);
            products = productService.buscarPorNombre(searchText);
        }
        model.addAttribute("products", products);
        return "product-search";
    }

    @GetMapping("/create")
    public String createProduct(Model model) {
        model.addAttribute("product", new Product());
        return "product-create";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("error", "* ERROR: No se puede eliminar el producto porque tiene otras entidades asociadas");
        return "redirect:/product/list";
    }
}
