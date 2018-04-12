package katapi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

@SpringBootTest
@AutoConfigureTestDatabase
public class KatapiAppTest implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(KatapiAppTest.class);


    public static void main(String[] args) {
        SpringApplication.run(KatapiAppTest.class, args);
    }

    //initialize database
    @Autowired
    JdbcTemplate jdbc;

    @Override
    public void run(String... strings) throws Exception {
        log.info("Creating tables");
        jdbc.execute("DROP TABLE IF EXISTS product");
        jdbc.execute("DROP TABLE IF EXISTS bill");
        jdbc.execute("DROP TABLE IF EXISTS orders");
        jdbc.execute("DROP TABLE IF EXISTS orders_content");

        jdbc.execute("CREATE TABLE product(id INTEGER PRIMARY KEY, name VARCHAR, price NUMERIC, weight NUMERIC)");
        jdbc.execute("CREATE TABLE bill(id INTEGER PRIMARY KEY, order_id INTEGER, amount NUMERIC, creation_date VARCHAR)");
        jdbc.execute("CREATE TABLE orders(id INTEGER PRIMARY KEY, status VARCHAR, shipment_amount NUMERIC, total_price NUMERIC)");
        jdbc.execute("CREATE TABLE orders_content(order_id INTEGER, product_id INTEGER)");
    }

}
