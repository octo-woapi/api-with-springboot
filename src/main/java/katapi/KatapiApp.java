package katapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;

import static com.fasterxml.jackson.databind.DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

@SpringBootApplication
public class KatapiApp implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(KatapiApp.class);

    @Bean
    public ObjectMapper jacksonBuilder() {
        return new ObjectMapper()
                .configure(USE_BIG_DECIMAL_FOR_FLOATS, true)
                .configure(WRITE_DATES_AS_TIMESTAMPS, false);
    }

    public static void main(String[] args) {
        SpringApplication.run(KatapiApp.class, args);
    }

    //initialize database
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public void run(String... strings) throws Exception {

        log.info("Creating tables");
        jdbc.execute("DROP TABLE IF EXISTS products");
        jdbc.execute("DROP TABLE IF EXISTS bills");
        jdbc.execute("DROP TABLE IF EXISTS orders");
        jdbc.execute("DROP TABLE IF EXISTS orders_content");

        jdbc.execute("CREATE TABLE products(id INTEGER PRIMARY KEY AUTO_INCREMENT, name VARCHAR, price NUMERIC, weight NUMERIC)");
        jdbc.execute("CREATE TABLE bills(id INTEGER PRIMARY KEY AUTO_INCREMENT, order_id INTEGER, amount NUMERIC, creation_date VARCHAR)");
        jdbc.execute("CREATE TABLE orders(id INTEGER PRIMARY KEY AUTO_INCREMENT, status VARCHAR, shipment_amount NUMERIC, total_price NUMERIC)");
        jdbc.execute("CREATE TABLE orders_content(order_id INTEGER AUTO_INCREMENT, product_id INTEGER)");

        log.info("Populating products");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (1, 'Cement bag 50kg', 25.0, 50.0)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (2, 'Cement bag 25kg', 15.0, 25.0)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (3, 'Red bricks small pallet 250 units', 149.99, 500.0)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (4, 'Concrete reinforcing rod 1 unit', 0.50, 0.80)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (5, 'Concrete reinforcing rod 25 units', 12.0, 20.0)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (6, 'Concrete blockwork 1 unit', 1.50, 19.0)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (7, 'Concrete blockwork small pallet 45 units', 45.0, 855.0)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (8, 'Screw box 25mm 100 units', 5.0, 1.0)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (9, 'Screw box 25mm 200 units', 10.0, 2.0)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (10, 'Screw box 75mm 50 units', 10.0, 2.5)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (11, 'Screw box 75mm 100 units', 20.0, 5.0)");
        jdbc.batchUpdate("INSERT INTO products (id, name, price, weight) values (12, 'Screw box 100mm 50 units', 25.0, 3.0)");

        log.info("Checking product count");
        log.info(jdbc.queryForObject("SELECT COUNT(*) FROM products", Integer.class).toString());

        log.info("Populating orders");
        jdbc.batchUpdate("INSERT INTO orders (id, status, shipment_amount, total_price) values (1, 'PENDING', 175.0, 25.0)");

        log.info("Populating orders content");
        jdbc.batchUpdate("INSERT INTO orders_content (order_id, product_id) values (1, 1)");

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
