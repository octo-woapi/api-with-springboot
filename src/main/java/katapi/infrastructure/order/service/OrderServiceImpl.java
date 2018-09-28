package katapi.infrastructure.order.service;

import katapi.domain.order.Order;
import katapi.infrastructure.order.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;

    @Override
    public List<Order> getAllOrders() {
        return orderDao.getAllOrders();
    }
}
