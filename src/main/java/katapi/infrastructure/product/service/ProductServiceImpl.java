package katapi.infrastructure.product.service;

import katapi.domain.product.Product;
import katapi.domain.product.ProductSortAttributes;
import katapi.infrastructure.product.exception.ProductNotFoundException;
import katapi.infrastructure.product.persistence.ProductDao;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    @Override
    public List<Product> getSortedAndPaginatedProductList(String sortParam, String range, int maxRange) {
        List<Product> productList = getSortedProductList(sortParam);
        return paginateProductList(productList, range, maxRange);
    }

    @Override
    public List<Product> paginateProductList(List<Product> productList, String range, int maxRange) {
        if(StringUtils.isEmpty(range)){
            return productList;
        }
        try {
            int startIndex = getStartIndexFromRange(range);
            int endIndex = getEndIndexFromRange(range);
            if(endIndex-startIndex > maxRange){
                endIndex = startIndex+maxRange;
            }
            return productList.subList(startIndex, endIndex);
        }catch (Exception e){
            return productList.subList(0, maxRange);
        }
    }

    /* ****************************************************************************************************************
                                                    PRIVATE METHODS
    **************************************************************************************************************** */


    private void validateProductParams(String name, BigDecimal price, BigDecimal weight){
        if(name == null) throw new IllegalArgumentException("Param 'name' is required");
        if(price == null) throw new IllegalArgumentException("Param 'price' is required");
        if(weight == null) throw new IllegalArgumentException("Param 'weight' is required");
    }

    private int getStartIndexFromRange(String range) {
        String startIndexString =  StringUtils.split(range, "-")[0];
        return Integer.parseInt(startIndexString);
    }

    private int getEndIndexFromRange(String range) {
        String startIndexString =  StringUtils.split(range, "-")[1];
        return Integer.parseInt(startIndexString);
    }

    private List<Product> getSortedProductList(String sortParam) {
        if (ProductSortAttributes.contains(sortParam)) {
            return getAllProducts()
                    .stream()
                    .sorted(Objects.requireNonNull(chooseAttributeToCompare(sortParam)))
                    .collect(Collectors.toList());
        } else {
            return getAllProducts();
        }
    }

    private Comparator<Product> chooseAttributeToCompare(@NotNull String sortParam){
        for (ProductSortAttributes attribute : ProductSortAttributes.values()) {
            if (attribute.getAttributeLowerCase().equals(sortParam)) {
                return attribute.getComparator();
            }
        }
        return null;
    }

}
