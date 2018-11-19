package katapi.infrastructure.product.service;

import katapi.domain.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    public List<Product> getAllProducts();

    public Product getProductById(Long id);

    public Product createProduct(String name, BigDecimal price, BigDecimal weight);

    public void deleteProduct(Long id);

    public List<Product> paginateProductList(List<Product> productList, String range, int maxRange);

    public List<Product> getSortedAndPaginatedProductList(String sortParam, String range, int maxRange);

}
