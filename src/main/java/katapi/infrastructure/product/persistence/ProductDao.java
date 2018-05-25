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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
public class ProductDao<T extends Product,ID extends Serializable> implements PagingAndSortingRepository<T,ID> {

    @Autowired
    JdbcTemplate jdbc;

    private String query = "SELECT * FROM Product";

    public List<Product> getAllProducts() {
        List<Product> result = jdbc.query(this.query, new BeanPropertyRowMapper(Product.class));
        return result;
    }

    public Product getProductById(Long id) {
        Product result = (Product) jdbc.queryForObject(
                "SELECT * FROM Product WHERE id = ?"
                , new Object[]{id}
                , new BeanPropertyRowMapper(Product.class));
        return result;
    }

    public Long insertProductAndReturnGeneratedID(Product product) {
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
        return jdbc.update("DELETE FROM Product WHERE id = ?", new Object[]{id});
    }

    @Override
    public Iterable<T> findAll(Sort sort) {

        String queryWithSort = this.query;

        for (Sort.Order o : sort) {
            queryWithSort += " ORDER BY " + o.getProperty() + " " + o.getDirection().toString() + " ";
        }

        return jdbc.query(queryWithSort, new BeanPropertyRowMapper(Product.class));
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        String queryPage = this.query;

        for (Sort.Order o : pageable.getSort()) {
            queryPage += " ORDER BY " + o.getProperty() + " " + o.getDirection().toString() + " ";
        }

        int pageSize = 10;

        queryPage += " LIMIT " + pageable.getPageNumber() * pageSize + " " + pageSize + " ";


        long count = count();

        return new JdbcPage<T>(pageable,
                (int) count / pageSize,
                (int) count,
                jdbc.query(queryPage, new BeanPropertyRowMapper(Product.class)));
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


    class JdbcPage<T> implements Page<T> {

        Pageable pageable;
        int totalPages;
        int totalNumbers;
        List<T> content;

        public JdbcPage(Pageable pageable, int totalPages,
                        int totalNumbers, List<T> content) {
            super();
            this.pageable = pageable;
            this.totalPages = totalPages;
            this.totalNumbers = totalNumbers;
            this.content = content;
        }

        @Override
        public int getTotalPages() {
            return 0;
        }

        @Override
        public long getTotalElements() {
            return 0;
        }

        @Override
        public int getNumber() {
            return 0;
        }

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public int getNumberOfElements() {
            return 0;
        }

        @Override
        public List<T> getContent() {
            return null;
        }

        @Override
        public boolean hasContent() {
            return false;
        }

        @Override
        public Sort getSort() {
            return null;
        }

        @Override
        public boolean isFirst() {
            return false;
        }

        @Override
        public boolean isLast() {
            return false;
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public Pageable nextPageable() {
            return null;
        }

        @Override
        public Pageable previousPageable() {
            return null;
        }

        @Override
        public <U> Page<U> map(Function<? super T, ? extends U> converter) {
            return null;
        }

        @Override
        public Iterator<T> iterator() {
            return null;
        }
    }
}
