package katapi.infrastructure.product.persistence;

import katapi.domain.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class ProductDao {

    @Autowired
    JdbcTemplate jdbc;

    public List<Product> getAllProducts() {
        List<Product> result = jdbc.query("SELECT * FROM Product", new BeanPropertyRowMapper(Product.class));
        return result;
    }

    public Product getProductById(Long id){
        Product result = (Product) jdbc.queryForObject("SELECT * FROM Product WHERE id = ?", new Object[] {id}, new BeanPropertyRowMapper(Product.class));
        return result;
    }
}
