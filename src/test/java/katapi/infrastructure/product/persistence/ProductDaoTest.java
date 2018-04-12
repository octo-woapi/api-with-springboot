package katapi.infrastructure.product.persistence;

import katapi.KatapiApp;
import katapi.KatapiAppTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = KatapiApp.class)
public class ProductDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ProductTestHelper testHelper;

    @BeforeClass
    public static void setUp(){
        //testHelper = new ProductTestHelper(jdbcTemplate);
        //testHelper.dropTableProduct();
        //testHelper.createTableProduct();
        //testHelper.insertTestProduct();
    }

    @AfterClass
    public static void tearDown(){
        //testHelper.clearTableProduct();
    }

    @Test
    public void nothing(){
        assertTrue(true);
    }

}