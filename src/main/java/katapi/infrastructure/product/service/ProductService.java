package katapi.infrastructure.product.service;

import katapi.domain.product.Product;
import katapi.infrastructure.product.exception.ProductNotFoundException;

import java.util.List;

public interface ProductService {

    public List<Product> getAllProducts();
    public Product getProductById(Long id);
    public Product createProduct(String name, Double price, Double weight);
    public void deleteProductFromItsID(Long id);
}
