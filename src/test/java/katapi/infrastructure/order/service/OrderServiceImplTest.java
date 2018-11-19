package katapi.infrastructure.order.service;

import katapi.KatapiApp;
import katapi.domain.order.Order;
import katapi.domain.order.OrderStatusEnum;
import katapi.domain.product.Product;
import katapi.infrastructure.order.exception.OrderNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = KatapiApp.class)
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class OrderServiceImplTest {

    @Autowired
    OrderService service;

    @Autowired
    JdbcTemplate jdbc;

    @Test
    public void createOrder_shouldAssociateProducts(){
        //given
        List<Product> productList = new ArrayList<Product>(){
            {
                add(new Product()); add(new Product()); add(new Product());
            }
        };
        //when
        Order tested = service.createOrder(productList);
        long orderId = tested.getId();
        int nbAssociations = jdbc.queryForObject("SELECT count(*) FROM orders_content WHERE order_id = ?", new Long[]{orderId}, Integer.class);

        //then
        assertThat(nbAssociations, is(3));
    }

    @Test
    public void createOrder_shouldSetStatusToPending(){
        //given
        List<Product> productList = new ArrayList<Product>(){
            {
                add(new Product()); add(new Product()); add(new Product());
            }
        };
        //when
        Order tested = service.createOrder(productList);
        //then
        assertThat(tested.getStatus(), is(OrderStatusEnum.PENDING));
    }

    @Test
    public void getOrderById_shouldGiveCorrespondigOrder(){
        //given
        Long orderId = 1l;
        //when
        Order order = service.getOrderById(orderId);
        //then
        assertThat(order.getId(), is(1l));
    }

    @Test(expected = OrderNotFoundException.class)
    public void getOrderById_shouldThrowOrderNotFoundExceptionIfNotFound(){
        //given
        Long orderId = 134l;
        //when
        Order order = service.getOrderById(orderId);
        //then
        //expect exception
    }

}