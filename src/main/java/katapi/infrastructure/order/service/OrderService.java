package katapi.infrastructure.order.service;

import katapi.domain.order.Order;
import katapi.domain.order.OrderStatusEnum;
import katapi.domain.product.Product;

import java.util.List;

public interface OrderService {

    public List<Order> getAllOrders();

    public Order getOrderById(long id);

    public Order createOrder(List<Product> productList);

    public void cancelOrder();

    public void payOrder();

    public void deleteOrder(long id);

}
