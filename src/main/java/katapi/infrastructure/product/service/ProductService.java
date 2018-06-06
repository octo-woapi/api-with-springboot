package katapi.infrastructure.product.service;

import katapi.domain.product.Product;
import org.springframework.data.domain.Page;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    public List<Product> getAllProducts();
    public Product getProductById(Long id);
    public Product createProduct(String name, BigDecimal price, BigDecimal weight);
    public void deleteProductFromItsID(Long id);

}
