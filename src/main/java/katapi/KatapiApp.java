package katapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.ResultSet;

@SpringBootApplication
public class KatapiApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(KatapiApp.class);


    public static void main(String[] args) {
        SpringApplication.run(KatapiApp.class, args);
    }

    //initialize database
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");
        jdbcTemplate.execute("DROP TABLE IF EXISTS product");
        jdbcTemplate.execute("DROP TABLE IF EXISTS bill");
        jdbcTemplate.execute("DROP TABLE IF EXISTS orders");
        jdbcTemplate.execute("DROP TABLE IF EXISTS orders_content");

        jdbcTemplate.execute("CREATE TABLE product(id INTEGER PRIMARY KEY, name VARCHAR, price NUMERIC, weight NUMERIC)");
        jdbcTemplate.execute("CREATE TABLE bill(id INTEGER PRIMARY KEY, order_id INTEGER, amount NUMERIC, creation_date VARCHAR)");
        jdbcTemplate.execute("CREATE TABLE orders(id INTEGER PRIMARY KEY, status VARCHAR, shipment_amount NUMERIC, total_price NUMERIC)");
        jdbcTemplate.execute("CREATE TABLE orders_content(order_id INTEGER, product_id INTEGER)");

        log.info("Populating products");
        jdbcTemplate.batchUpdate("INSERT INTO product (id, name, price, weight) values (1, 'Cement bag 50kg', 25.0, 50.0)");
        jdbcTemplate.batchUpdate("INSERT INTO product (id, name, price, weight) values (2, 'Cement bag 25kg', 15.0, 25.0)");
        jdbcTemplate.batchUpdate("INSERT INTO product (id, name, price, weight) values (3, 'Red bricks small pallet 250 units', 149.99, 500.0)");
        jdbcTemplate.batchUpdate("INSERT INTO product (id, name, price, weight) values (4, 'Concrete reinforcing rod 1 unit', 0.50, 0.80)");
        jdbcTemplate.batchUpdate("INSERT INTO product (id, name, price, weight) values (5, 'Concrete reinforcing rod 25 units', 12.0, 20.0)");
        jdbcTemplate.batchUpdate("INSERT INTO product (id, name, price, weight) values (6, 'Concrete blockwork 1 unit', 1.50, 19.0)");
        jdbcTemplate.batchUpdate("INSERT INTO product (id, name, price, weight) values (7, 'Concrete blockwork small pallet 45 units', 45.0, 855.0)");

        log.info("Checking product count");
        log.info(jdbcTemplate.queryForObject("SELECT COUNT(*) FROM product", Integer.class).toString());

        /*
        CREATE TABLE product(id INTEGER PRIMARY KEY, name VARCHAR, price NUMERIC, weight NUMERIC);
        CREATE TABLE bill(id INTEGER PRIMARY KEY, order_id INTEGER, amount NUMERIC, creation_date VARCHAR);
        CREATE TABLE orders(id INTEGER PRIMARY KEY, status VARCHAR, shipment_amount NUMERIC, total_price NUMERIC);
        CREATE TABLE orders_content(order_id INTEGER, product_id INTEGER);
         */

        /*
        INSERT INTO product (id, name, price, weight values (1, 'Cement bag 50kg', 25.0, 50.0);
        INSERT INTO product (id, name, price, weight values (2, 'Cement bag 25kg', 15.0, 25.0);
        INSERT INTO product (id, name, price, weight values (3, 'Red bricks small pallet 250 units', 149.99, 500.0);
        INSERT INTO product (id, name, price, weight values (4, 'Concrete reinforcing rod 1 unit', 0.50, 0.80);
        INSERT INTO product (id, name, price, weight values (5, 'Concrete reinforcing rod 25 units', 12.0, 20.0);
        INSERT INTO product (id, name, price, weight values (6, 'Concrete blockwork 1 unit', 1.50, 19.0);
        INSERT INTO product (id, name, price, weight values (7, 'Concrete blockwork small pallet 45 units', 45.0, 855.0);
         */
    }
}
