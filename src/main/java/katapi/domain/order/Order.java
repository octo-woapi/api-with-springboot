package katapi.domain.order;

import katapi.domain.product.Product;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private OrderStatusEnum status;
    private List<Product> productList;
    private BigDecimal shipmentAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalWeight;



    public BigDecimal getTotalAmount() {
        if(CollectionUtils.isEmpty(productList)){return new BigDecimal("0.00");}

        BigDecimal total = productList.stream().map(product -> product.getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, BigDecimal.ROUND_HALF_UP);

        if(total.compareTo(new BigDecimal("1000")) > 0){
            return total.subtract(total.multiply(new BigDecimal("0.05"))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }else {
            return total;
        }
    }

    public void addProduct(Product product) {
        if(null == productList){
            productList = new ArrayList<Product>();
        }
        productList.add(product);
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    public BigDecimal getShipmentAmount() {
        return shipmentAmount;
    }

    public void setShipmentAmount(BigDecimal shipmentAmount) {
        this.shipmentAmount = shipmentAmount;
    }

    public BigDecimal getTotalWeight() {
        return totalWeight;
    }

    public void setTotalWeight(BigDecimal totalWeight) {
        this.totalWeight = totalWeight;
    }

    public OrderStatusEnum getStatus() {
        return status;
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status;
    }

    public Order() {
    }

    public Order(List<Product> productList, BigDecimal shipmentAmount, BigDecimal totalAmount, BigDecimal totalWeight) {
        this.productList = productList;
        this.shipmentAmount = shipmentAmount;
        this.totalAmount = totalAmount;
        this.totalWeight = totalWeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id) &&
                status == order.status &&
                Objects.equals(productList, order.productList) &&
                Objects.equals(shipmentAmount, order.shipmentAmount) &&
                Objects.equals(totalAmount, order.totalAmount) &&
                Objects.equals(totalWeight, order.totalWeight);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, status, productList, shipmentAmount, totalAmount, totalWeight);
    }

}
