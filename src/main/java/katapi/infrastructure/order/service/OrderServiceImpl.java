package katapi.infrastructure.order.service;

import katapi.domain.order.Order;
import katapi.domain.product.Product;
import katapi.infrastructure.order.exception.OrderNotFoundException;
import katapi.infrastructure.order.persistence.OrderDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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

    @Override
    public Order getOrderById(long id) {
        Order order = null;
        try{
            order = orderDao.getOrderById(id);
        }catch(EmptyResultDataAccessException e){
            throw new OrderNotFoundException(id);
        }
        return order;
    }

    @Override
    public Order createOrder(List<Product> productList) {
        Order created = new Order(productList);
        orderDao.insertOrder(created);
        return created;
    }

    @Override
    public void cancelOrder() {

    }

    @Override
    public void payOrder() {

    }

    @Override
    public void deleteOrder(long id) {

    }
}
