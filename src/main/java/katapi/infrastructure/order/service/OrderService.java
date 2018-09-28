package katapi.infrastructure.order.service;

import katapi.domain.order.Order;
import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

}
