package katapi.infrastructure.product.service;

import katapi.domain.product.Product;
import katapi.infrastructure.product.exception.ProductNotFoundException;
import katapi.infrastructure.product.persistence.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductDao productDao;

    @Override
    public List<Product> getAllProducts() {
        return productDao.getAllProducts();
    }

    @Override
    public Product getProductById(Long id) throws ProductNotFoundException{
        Product result = null;
        try{
            result = productDao.getProductById(id);
        }catch(EmptyResultDataAccessException e){
            throw new ProductNotFoundException(id);
        }
        return result;
    }

    @Override
    public Product createProduct(String name, Double price, Double weight) {
        Product product = new Product(null, name, price, weight);
        Long generatedId = productDao.insertProductAndReturnGeneratedID(product);
        product.setId(generatedId);
        return product;
    }
}
