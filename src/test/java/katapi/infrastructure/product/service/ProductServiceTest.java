package katapi.infrastructure.product.service;

import katapi.KatapiApp;
import katapi.domain.product.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KatapiApp.class)
@WebAppConfiguration
public class ProductServiceTest {

    @Autowired
    ProductService service;

    @Test
    public void getAllProducts_shouldReturnAllTuples(){
        // WHEN
        List<Product> allProducts = service.getAllProducts();
        // THEN
        assertThat(allProducts.size(), is(7));
    }

}