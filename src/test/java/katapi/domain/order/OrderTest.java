package katapi.domain.order;

import katapi.domain.product.Product;
import org.junit.Ignore;
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
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class OrderTest {

    private static final Logger log = LoggerFactory.getLogger(OrderTest.class);

    /* ****************** Adding products **************** */

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

    /* ****************** Product amount **************** */

    @Test
    public void getProductAmount_shouldReturnTheSumOfAllProductsPrice(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setPrice(new BigDecimal(5));
        Product testProduct2 = new Product();
        testProduct2.setPrice(new BigDecimal(1.3));

        tested.addProduct(testProduct1);
        tested.addProduct(testProduct1);
        tested.addProduct(testProduct1);
        tested.addProduct(testProduct2);
        //when
        BigDecimal productAmount = tested.getProductAmount();
        //then
        assertThat(productAmount, is(new BigDecimal("16.30")));
    }

    @Test
    public void getProductAmount_shouldReturn0IfNoProduct(){
        //given
        Order tested = new Order();
        //when
        BigDecimal productAmount = tested.getProductAmount();
        //then
        assertThat(productAmount, is(BigDecimal.ZERO));
    }

    /* ****************** Total weight **************** */

    @Test
    public void getTotalWeight_shouldReturnZeroIfNoProduct(){
        //given
        Order tested = new Order();
        //when
        BigDecimal totalWeight = tested.getTotalWeight();
        //then
        assertThat(totalWeight, is(BigDecimal.ZERO));
    }

    @Test
    public void getTotalWeight_shouldReturnTotalWeight(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setWeight(new BigDecimal(50.0));
        Product testProduct2 = new Product();
        testProduct2.setWeight(new BigDecimal(51.13));

        tested.addProduct(testProduct1);
        tested.addProduct(testProduct2);
        //when
        BigDecimal totalWeight = tested.getTotalWeight();
        //then
        assertThat(totalWeight, is(new BigDecimal("101.13")));
    }

    /* ****************** Shimpent amount **************** */

    @Test
    public void orderShipmentAmount_shouldBeZeroIfOrderTotalWeightLT10kg(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setWeight(new BigDecimal(5));
        tested.addProduct(testProduct1);
        //when
        BigDecimal shipmentAmount = tested.getShipmentAmount();
        //then
        assertEquals(shipmentAmount, BigDecimal.ZERO);
    }

    @Test
    public void orderShipmentAmount_shouldBeZero0ifWeightIsZero(){
        //given
        Order tested = new Order();
        //when
        BigDecimal shipmentAmount = tested.getShipmentAmount();
        //then
        assertEquals(shipmentAmount, BigDecimal.ZERO);
    }

    @Test
    public void orderShipmentAmount_shouldBe25IfOrderTotalWeightIsExactly10kg(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setWeight(new BigDecimal(10.0));
        tested.addProduct(testProduct1);
        //when
        BigDecimal shipmentAmount = tested.getShipmentAmount();
        //then
        assertThat(shipmentAmount, is(new BigDecimal("25")));
    }

    @Test
    public void orderShipmentAmount_shouldBe25IfOrderTotalWeightGT10kg(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setWeight(new BigDecimal(12.5));
        tested.addProduct(testProduct1);
        //when
        BigDecimal shipmentAmount = tested.getShipmentAmount();
        //then
        assertThat(shipmentAmount, is(new BigDecimal("25")));
    }

    @Test
    public void orderShipmentAmount_shouldBe50IfOrderTotalWeightGT20kg(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setWeight(new BigDecimal(22.5));
        tested.addProduct(testProduct1);
        //when
        BigDecimal shipmentAmount = tested.getShipmentAmount();
        //then
        assertThat(shipmentAmount, is(new BigDecimal("50")));
    }

    @Test
    public void orderShipmentAmount_shouldBe25MoreEach10kg(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setWeight(new BigDecimal(12.5));
        tested.addProduct(testProduct1);

        //when-then
        BigDecimal shipmentAmount = tested.getShipmentAmount();
        assertThat(shipmentAmount, is(new BigDecimal("25")));

        Product testProduct2 = new Product();
        testProduct2.setWeight(new BigDecimal(10));
        tested.addProduct(testProduct2);
        shipmentAmount = tested.getShipmentAmount();
        assertThat(shipmentAmount, is(new BigDecimal("50")));

        Product testProduct3 = new Product();
        testProduct3.setWeight(new BigDecimal(90));
        tested.addProduct(testProduct3);
        shipmentAmount = tested.getShipmentAmount();
        assertThat(shipmentAmount, is(new BigDecimal("275")));
    }

    /* ************** Total amount with discount ************** */

    @Test
    public void orderTotalAmount_shouldHave5PercentDiscountWhenTotalPriceExceed1000(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setPrice(new BigDecimal(500.0));
        Product testProduct2 = new Product();
        testProduct2.setPrice(new BigDecimal(501.0));

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
        BigDecimal totalRandom = randomPrice.add(randomPrice2);
        BigDecimal totalWithDiscount = totalRandom.subtract(totalRandom.multiply(new BigDecimal("0.05"))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
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
        testProduct1.setPrice(new BigDecimal(50.0));
        Product testProduct2 = new Product();
        testProduct2.setPrice(new BigDecimal(501.0));

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
        testProduct1.setPrice(new BigDecimal(50.45));
        Product testProduct2 = new Product();
        testProduct2.setPrice(new BigDecimal(65.034));
        Product testProduct3 = new Product();
        testProduct3.setPrice(new BigDecimal(3.543));

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
        assertThat(totalAmount, is(BigDecimal.ZERO));
    }

    @Test
    public void orderTotalAmount_shouldAddShipmentToPrice(){
        //given
        Order tested = new Order();
        Product testProduct1 = new Product();
        testProduct1.setPrice(new BigDecimal(50.45));
        testProduct1.setWeight(new BigDecimal(10));
        Product testProduct2 = new Product();
        testProduct2.setPrice(new BigDecimal(65.034));
        testProduct2.setWeight(new BigDecimal(12));

        tested.addProduct(testProduct1);
        tested.addProduct(testProduct2);

        // when
        BigDecimal totalAmount = tested.getTotalAmount();

        //then 110.484 + 50
        assertThat(totalAmount, is(new BigDecimal("165.48")));
    }

    

}