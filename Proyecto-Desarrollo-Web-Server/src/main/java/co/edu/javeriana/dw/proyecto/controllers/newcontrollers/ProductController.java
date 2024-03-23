package co.edu.javeriana.dw.proyecto.controllers.news;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.edu.javeriana.dw.proyecto.model.Product;
import co.edu.javeriana.dw.proyecto.service.ProductService;
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

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    // http://localhost:8080/api/product/list
    @GetMapping("/list")
    public List<Product> listProducts() {
        return productService.getAllProduct();
    }

    @GetMapping("/list-page")
    public Page<Product> getAllProducts(Pageable pageable) {
        return productService.listarProductosPaginable(pageable);
    }

    // https://www.baeldung.com/spring-rest-openapi-documentation
    @GetMapping("/search")
    public Page<Product> searchProduct(@RequestParam String nombre, Pageable pageable) {
        return productService.buscarProducto(nombre, pageable);
    }



    // http://localhost:8080/api/product/2
    @Operation(summary = "Buscar producto por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Encontró el producto", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Product.class)) }),
            @ApiResponse(responseCode = "400", description = "Id suministrado es invalido", content = @Content),
            @ApiResponse(responseCode = "404", description = "Producto no encontrada", content = @Content) })

    @GetMapping("/{id}")
    public Product recoverProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("")
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long idProducto) {
        productService.deleteProduct(idProducto);
    }

    @PutMapping("")
    public Product updateProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @PatchMapping("{id}/nombre")
    public Map<String, Object> modifyName(@PathVariable Long id, @RequestBody String nuevoNombre) {
        int numeroRegistrosModificados = productService.actualizarNombreProducto(id, nuevoNombre);
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("cantidadTuplasModificadas", numeroRegistrosModificados);
        return respuesta;
    }
}
