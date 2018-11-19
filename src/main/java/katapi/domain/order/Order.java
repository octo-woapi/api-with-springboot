package katapi.domain.order;

import katapi.domain.product.Product;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {

    private Long id;
    private OrderStatusEnum status = OrderStatusEnum.PENDING;
    private List<Product> productList;
    private BigDecimal shipmentAmount;
    private BigDecimal totalAmount;
    private BigDecimal totalWeight;

    /**
     * Returns the total amount of the order, taking in account shipment and discount
     * @return BigDecimal
     */
    public BigDecimal getTotalAmount() {
        BigDecimal total = getProductAmount().add(getShipmentAmount());

        if(total.compareTo(BigDecimal.valueOf(1000.0)) > 0){
            return total.subtract(total.multiply(new BigDecimal("0.05"))).setScale(2, BigDecimal.ROUND_HALF_EVEN);
        }else {
            return total;
        }
    }

    /**
     * Returns the price of all products in the order, rounded to 2 decimals
     * @return BigDecimal
     */
    public BigDecimal getProductAmount(){
        if(CollectionUtils.isEmpty(productList)) { return BigDecimal.ZERO; }
        return productList.stream()
                .map(product -> product.getPrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Returns the shipment amount (25 every 10 kg)
     * @return BigDecimal
     */
    public BigDecimal getShipmentAmount() {
        return new BigDecimal(
                ((int)Math.floor(getTotalWeight().intValue()) / 10) * 25
        );
    }

    /**
     * Returns total weight of products. Returns 0 if no products
     * @return BigDecimal
     */
    public BigDecimal getTotalWeight() {
        if(CollectionUtils.isEmpty(productList)) { return BigDecimal.ZERO; }
        return productList.stream()
                .map(product -> product.getWeight())
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Adds a product to the product list. Initialize this product list if necessary
     * @param product
     */
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

    public void setShipmentAmount(BigDecimal shipmentAmount) {
        this.shipmentAmount = shipmentAmount;
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

    public Order(List<Product> productList) {
        this.productList = productList;
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
