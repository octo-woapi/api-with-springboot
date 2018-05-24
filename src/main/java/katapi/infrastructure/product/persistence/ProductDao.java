package katapi.infrastructure.product.persistence;

import katapi.domain.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductDao  {

    @Autowired
    JdbcTemplate jdbc;

    public List<Product> getAllProducts() {
        List<Product> result = jdbc.query("SELECT * FROM Product", new BeanPropertyRowMapper(Product.class));
        return result;
    }

    public Product getProductById(Long id){
        Product result = (Product) jdbc.queryForObject(
                "SELECT * FROM Product WHERE id = ?"
                , new Object[] {id}
                , new BeanPropertyRowMapper(Product.class));
        return result;
    }

    public Long insertProductAndReturnGeneratedID(Product product){
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO Product (name, price, weight) VALUES (?, ?, ?)",
                    new String[]{"id"});
            ps.setString(1, product.getName());
            ps.setDouble(2, product.getPrice().doubleValue());
            ps.setDouble(3, product.getWeight().doubleValue());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public int deleteProductFromItsID(Long id) {
        return jdbc.update("DELETE FROM Product WHERE id = ?", new Object[] {id});
    }
}
