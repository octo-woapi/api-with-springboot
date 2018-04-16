package katapi.infrastructure.product.service;

import katapi.KatapiApp;
import katapi.domain.product.Product;
import katapi.infrastructure.product.exception.ProductNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
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

    @Test
    public void getProductById_shouldReturnTheCorrectObject(){
        // GIVEN
        Long productId = 1l;
        // WHEN
        Product product = service.getProductById(productId);
        // THEN
        assertThat(product.getId(), is(productId));
        assertThat(product.getName(),is("Cement bag 50kg"));
    }

    @Test(expected = ProductNotFoundException.class)
    public void getProductById_shouldThrowProductNotFoundExceptionIfNotFound(){
        // GIVEN
        Long productId = 14l;
        // WHEN
        Product product = service.getProductById(productId);
        // THEN
        //expect exception
    }

}