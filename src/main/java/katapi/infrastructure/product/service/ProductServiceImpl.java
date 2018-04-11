package katapi.infrastructure.product.service;

import katapi.domain.product.Product;
import katapi.infrastructure.product.persistence.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ProductServiceImpl implements ProductServiceInterface {

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }
}
