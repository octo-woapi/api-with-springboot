package katapi.domain.order;

import katapi.domain.product.Product;
import katapi.infrastructure.product.rest.ProductResource;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

public class OrderTest {

    @Test
    public void addProduct_shouldCreateNewProductListIfItsNull(){
        //given
        Order tested = new Order();
        //when
        tested.addProduct(new Product());
        //
        assertThat(tested.getProductList(), is(notNullValue()));
    }

    @Test
    public void addProduct_shouldIncreaseProductListBy1(){
        //given
        Order tested = new Order();
        ArrayList<Product> testList = new ArrayList<>(Arrays.asList(new Product(), new Product(), new Product()));
        tested.setProductList(testList);
        //when
        tested.addProduct(new Product());
        //Then
        assertThat(tested.getProductList().size(), is(4));
    }

    @Test
    public void orderTotalAmount_shouldHave5PercentDiscountWhenTotalPriceExceed1000(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setPrice(new BigDecimal("500.0"));
        Product testProduct2 = new Product();
        testProduct2.setPrice(new BigDecimal("501.0"));

        tested.addProduct(testProduct1);
        tested.addProduct(testProduct2);
        //when
        BigDecimal totalAmount = tested.getTotalAmount();
        //then
        assertThat(totalAmount, is(new BigDecimal("950.95")));
    }

    @Test
    public void orderTotalAmount_shouldHaveNoDiscountUnder1000Credits(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setPrice(new BigDecimal("50.0"));
        Product testProduct2 = new Product();
        testProduct2.setPrice(new BigDecimal("501.0"));

        tested.addProduct(testProduct1);
        tested.addProduct(testProduct2);
        //when
        BigDecimal totalAmount = tested.getTotalAmount();
        //then
        assertThat(totalAmount, is(new BigDecimal("551.0")));
    }

    @Test
    public void orderTotalAmount_shouldBeTheExactAdditionOfProductPrice(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setPrice(new BigDecimal("50.45"));
        Product testProduct2 = new Product();
        testProduct2.setPrice(new BigDecimal("65.034"));
        Product testProduct3 = new Product();
        testProduct3.setPrice(new BigDecimal("3.543"));

        tested.addProduct(testProduct1);
        tested.addProduct(testProduct2);
        tested.addProduct(testProduct3);
        //when
        BigDecimal totalAmount = tested.getTotalAmount();
        //then
        assertThat(totalAmount, is(new BigDecimal("119.027")));
    }

    @Test
    public void orderTotalAmount_shouldBeZeroIfOrderHasNoProduct(){
        //given
        Order tested = new Order();
        //when
        BigDecimal totalAmount = tested.getTotalAmount();
        //then
        assertThat(totalAmount, is(new BigDecimal("0.0")));
    }

}