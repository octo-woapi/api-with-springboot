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
public class ProductDao<T extends Product,ID extends Serializable> implements PagingAndSortingRepository<T,ID>  {

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

    @Override
    public Iterable<T> findAll(Sort sort) {
        String query = "SELECT * FROM Product";

        for (Sort.Order o : sort) {
            query += " ORDER BY " + o.getProperty() + " " + o.getDirection().toString() + " ";
        }

        return jdbc.query(query, new BeanPropertyRowMapper(Product.class));
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends T> S save(S entity) {
        return null;
    }

    @Override
    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<T> findById(ID id) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(ID id) {
        return false;
    }

    @Override
    public Iterable<T> findAll() {
        return null;
    }

    @Override
    public Iterable<T> findAllById(Iterable<ID> ids) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(ID id) {

    }

    @Override
    public void delete(T entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {

    }

    @Override
    public void deleteAll() {

    }
}
