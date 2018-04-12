package katapi.infrastructure.product.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

public class ProductTestHelper {

    private JdbcTemplate jdbc;

    @Autowired
    public ProductTestHelper(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void clearTableProduct(){
        jdbc.execute("TRUNCATE TABLE product");
    }

    public void insertTestProduct(){
        jdbc.batchUpdate("INSERT INTO product (id, name, price, weight) values (1, 'Cement bag 50kg', 25.0, 50.0)");
        jdbc.batchUpdate("INSERT INTO product (id, name, price, weight) values (2, 'Cement bag 25kg', 15.0, 25.0)");
        jdbc.batchUpdate("INSERT INTO product (id, name, price, weight) values (3, 'Red bricks small pallet 250 units', 149.99, 500.0)");
        jdbc.batchUpdate("INSERT INTO product (id, name, price, weight) values (4, 'Concrete reinforcing rod 1 unit', 0.50, 0.80)");
        //jdbc.batchUpdate("INSERT INTO product (id, name, price, weight) values (5, 'Concrete reinforcing rod 25 units', 12.0, 20.0)");
        jdbc.batchUpdate("INSERT INTO product (id, name, price, weight) values (6, 'Concrete blockwork 1 unit', 1.50, 19.0)");
        //jdbc.batchUpdate("INSERT INTO product (id, name, price, weight) values (7, 'Concrete blockwork small pallet 45 units', 45.0, 855.0)");
    }

    public void createTableProduct(){
        jdbc.execute("CREATE TABLE product(id INTEGER PRIMARY KEY, name VARCHAR, price NUMERIC, weight NUMERIC)");
    }

    public void dropTableProduct(){
        jdbc.execute("DROP TABLE IF EXISTS product");
    }
}
