package co.edu.javeriana.dw.proyecto.service;

import co.edu.javeriana.dw.proyecto.model.Product;
import co.edu.javeriana.dw.proyecto.model.Star;
import co.edu.javeriana.dw.proyecto.persistence.IProductRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private IProductRepository productRepository;

    public List<Product> getAllProduct() {
        return productRepository.findAll();
    }
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    public int actualizarNombreProducto(Long id, String name) {
        return productRepository.updateProductName(id, name);
    }
    public Page<Product> listarProductosPaginable(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Page<Product> buscarProducto(String name, Pageable pageable) {
        return productRepository.findAllByNameStartingWithIgnoreCase(name, pageable);
    }

}
