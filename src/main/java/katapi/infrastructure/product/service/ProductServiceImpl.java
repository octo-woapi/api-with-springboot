package katapi.infrastructure.product.service;

import katapi.domain.product.Product;
import katapi.infrastructure.product.exception.ProductNotFoundException;
import katapi.infrastructure.product.persistence.ProductDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.awt.print.Pageable;
import java.math.BigDecimal;
import java.util.List;

@Component
public class ProductServiceImpl implements ProductService{

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    private ProductDao productDao;


    @Override
    public List<Product> getAllProducts() { return productDao.getAllProducts(); }

    @Override
    public Product getProductById(Long id){
        Product result = null;
        try{
            result = productDao.getProductById(id);
        }catch(EmptyResultDataAccessException e){
            throw new ProductNotFoundException(id);
        }
        return result;
    }

    @Override
    public Product createProduct(String name, BigDecimal price, BigDecimal weight){
        validateProductParams(name, price, weight);
        Product product = new Product(null, name, price, weight);
        Long generatedId = productDao.insertProductAndReturnGeneratedID(product);
        product.setId(generatedId);
        return product;
    }

    @Override
    public void deleteProductFromItsID(Long id) {
        int nbRowsDeleted = productDao.deleteProductFromItsID(id);
        if(nbRowsDeleted == 0){ throw new ProductNotFoundException(id); }
    }


    private void validateProductParams(String name, BigDecimal price, BigDecimal weight){
        if(name == null) throw new IllegalArgumentException("Param 'name' is required");
        if(price == null) throw new IllegalArgumentException("Param 'price' is required");
        if(weight == null) throw new IllegalArgumentException("Param 'weight' is required");
    }

}
