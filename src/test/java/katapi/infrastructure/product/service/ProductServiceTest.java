package katapi.infrastructure.product.service;

import katapi.KatapiApp;
import katapi.domain.product.Product;
import katapi.infrastructure.product.exception.ProductNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KatapiApp.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ProductServiceTest {

    @Autowired
    ProductService service;

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    public void getAllProducts_shouldReturnAllTuples(){
        //when
        List<Product> allProducts = service.getAllProducts();
        //then
        assertThat(allProducts.size(), is(7));
    }

    @Test
    public void getProductById_shouldReturnTheCorrectObject(){
        //given
        Long productId = 1l;
        //when
        Product product = service.getProductById(productId);
        //then
        assertThat(product.getId(), is(productId));
        assertThat(product.getName(),is("Cement bag 50kg"));
    }

    @Test(expected = ProductNotFoundException.class)
    public void getProductById_shouldThrowProductNotFoundExceptionIfNotFound(){
        //given
        Long productId = 14l;
        //when
        Product product = service.getProductById(productId);
        //then
        //expect exception
    }

    @Test
    public void createProduct_shouldReturnAnObjectProduct(){
        //when
        Object tested = service.createProduct("test", BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0));
        //then
        assertThat(tested, is(instanceOf(Product.class)));
    }

    @Test
    public void createProduct_shouldCreateAProductWithAnID(){
        //when
        Product tested = service.createProduct("test", BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0));
        //then
        assertThat(tested.getId(), not(nullValue()));
    }

    @Test
    public void createProduct_shouldReturnAProductWithCorrectValues(){
        //given
        String name = "name To Be Tested";
        BigDecimal price = BigDecimal.valueOf(23.75);
        BigDecimal weight = BigDecimal.valueOf(5.234234);
        //when
        Product tested = service.createProduct(name, price, weight);
        //then
        assertThat(tested.getName(), is(name.toString()));
        assertThat(tested.getPrice().doubleValue(), is(price.doubleValue()));
        assertThat(tested.getWeight().doubleValue(), is(weight.doubleValue()));
    }

    @Test
    public void createProduct_shouldInsert1NewProductInDB(){
        //given
        int howManyProducts = service.getAllProducts().size();
        //when
        service.createProduct("test", BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0));
        //then
        assertThat(service.getAllProducts().size(), is(howManyProducts+1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProduct_shouldThrowAnIllegalArgumentExceptionIfNameMissing(){
        //when
        service.createProduct(null, BigDecimal.valueOf(1.0), BigDecimal.valueOf(1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProduct_shouldThrowAnIllegalArgumentExceptionIfPriceMissing(){
        //when
        service.createProduct("tested", null, BigDecimal.valueOf(1.0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createProduct_shouldThrowAnIllegalArgumentExceptionIfWeightMissing(){
        //when
        service.createProduct("tested", BigDecimal.valueOf(1.0), null);
    }

    @Test
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deleteProduct_shouldDeleteOneProductInDB(){
        //given
        int howManyProducts = service.getAllProducts().size();
        Long idToBeDeleted = 3l;
        //when
        service.deleteProductFromItsID(idToBeDeleted);
        //then
        assertThat(service.getAllProducts().size(), is(howManyProducts-1));
    }

    @Test(expected = ProductNotFoundException.class)
    public void deleteProduct_shouldThrowProductNotFoundIfProductNotExists(){
        //given
        Long idToBeDeleted = 334l;
        //when
        service.deleteProductFromItsID(idToBeDeleted);
        //then exception
    }

}