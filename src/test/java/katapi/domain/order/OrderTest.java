package katapi.domain.order;

import katapi.domain.product.Product;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class OrderTest {

    private static final Logger log = LoggerFactory.getLogger(OrderTest.class);


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
    public void orderTotalAmount_shouldHave5PercentDiscountWhenTotalPriceExceed1000ForAnyData(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        BigDecimal randomPrice = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(500.0, 1000.0)).setScale(4, RoundingMode.HALF_UP);
        testProduct1.setPrice(randomPrice);
        BigDecimal randomPrice2 = BigDecimal.valueOf(ThreadLocalRandom.current().nextDouble(500.0, 1000.0)).setScale(4, RoundingMode.HALF_UP);
        Product testProduct2 = new Product();
        testProduct2.setPrice(randomPrice2);

        tested.addProduct(testProduct1);
        tested.addProduct(testProduct2);
        //when
        BigDecimal totalAmount = tested.getTotalAmount();
        BigDecimal totalTested = randomPrice.add(randomPrice2);
        BigDecimal totalWithDiscount = totalTested.subtract(totalTested.multiply(new BigDecimal("0.05"))).setScale(2, BigDecimal.ROUND_HALF_UP);
        //then
        assertThat(totalAmount, is(totalWithDiscount));
    }

    //Totally arbitrary to have 2 digits in the decimal part
    @Test
    public void orderTotalAmount_shouldHaveAMaximumOf2DigitDecimal(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setPrice(new BigDecimal("1.002084823747"));
        Product testProduct2 = new Product();
        testProduct2.setPrice(new BigDecimal("1.120000000345"));

        tested.addProduct(testProduct1);
        tested.addProduct(testProduct2);
        //when
        BigDecimal totalAmount = tested.getTotalAmount();
        //then
        assertThat(totalAmount, is(new BigDecimal("2.12")));
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
        assertThat(totalAmount, is(new BigDecimal("551.00")));
    }

    @Test
    public void orderTotalAmount_shouldBeTheAdditionOfProductPriceRoundedFor2digits(){
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
        assertThat(totalAmount, is(new BigDecimal("119.03")));
    }

    @Test
    public void orderTotalAmount_shouldBeZeroIfOrderHasNoProduct(){
        //given
        Order tested = new Order();
        //when
        BigDecimal totalAmount = tested.getTotalAmount();
        //then
        assertThat(totalAmount, is(new BigDecimal("0.00")));
    }

    

}