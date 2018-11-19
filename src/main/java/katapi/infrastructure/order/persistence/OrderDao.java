package katapi.infrastructure.order.persistence;

import katapi.domain.order.Order;
import katapi.domain.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.util.List;

@Repository
public class OrderDao {

    @Autowired
    JdbcTemplate jdbc;

    public List<Order> getAllOrders() {
        List<Order> result = jdbc.query("SELECT * FROM Orders", new BeanPropertyRowMapper(Order.class));
        return result;
    }

    public Order getOrderById(long id) {
        Order result = (Order) jdbc.queryForObject(
                "SELECT * FROM Orders WHERE id = ?"
                , new Object[] {id}
                , new BeanPropertyRowMapper(Order.class));
        return result;
    }

    public Long insertOrder(Order order){
        GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO Orders (status, shipment_amount, total_price) VALUES (?, ?, ?)", new String[]{"id"});

            ps.setString(1, order.getStatus().toString());
            ps.setDouble(2, order.getShipmentAmount().doubleValue());
            ps.setDouble(3, order.getTotalAmount().doubleValue());

            return ps;

        }, keyHolder);

        Long orderId = keyHolder.getKey().longValue();
        order.setId(orderId);

        // for each instead of map because products are not collected afterward
        order.getProductList().forEach(product -> associateProductToOrder(product, orderId));

        return orderId;
    }

    private void associateProductToOrder(Product product, Long orderId) {
        jdbc.update("INSERT INTO orders_content(order_id, product_id) VALUES (?, ?)",
                orderId,
                product.getId());
    }

    public int deleteOrder(Long id) {
        return jdbc.update("DELETE FROM Orders WHERE id = ?", new Object[] {id});
    }


}
