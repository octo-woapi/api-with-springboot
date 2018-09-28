package katapi.infrastructure.order.persistence;

import katapi.domain.order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderDao {

    @Autowired
    JdbcTemplate jdbc;

    public List<Order> getAllOrders() {
        List<Order> result = jdbc.query("SELECT * FROM Orders", new BeanPropertyRowMapper(Order.class));
        return result;
    }
}
